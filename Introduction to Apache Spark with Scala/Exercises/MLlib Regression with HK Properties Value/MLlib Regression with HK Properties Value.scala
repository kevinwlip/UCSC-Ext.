// Databricks notebook source
// MAGIC %md ### Use Machine Learning Regression task to predict HK properties values
// MAGIC * Using HK Central, Sheung Wan and Sai Wan private housing dataset
// MAGIC * <a href="https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.package" target="_new">Apache Spark Scala Doc</a>

// COMMAND ----------

import org.apache.spark.sql._
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.lit
import org.apache.spark.sql.functions.udf

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.feature.{OneHotEncoder, VectorAssembler, StringIndexer}
import org.apache.spark.ml.feature.OneHotEncoderEstimator
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionModel}
import org.apache.spark.ml.regression.{DecisionTreeRegressor, DecisionTreeRegressionModel}

import org.apache.spark.ml.regression.RandomForestRegressor
import org.apache.spark.ml.regression.{GBTRegressionModel, GBTRegressor}
import org.apache.spark.ml.evaluation.RegressionEvaluator

// COMMAND ----------

val dataFolder = "/FileStore/tables/mllib"

// COMMAND ----------

// reading the data file
val hkPropertiesDF = spark.read
                          .option("header","true")
                          .option("inferSchema", "true")
                          .csv(s"$dataFolder/hk_properties.csv").cache

// COMMAND ----------

hkPropertiesDF.printSchema
// what featues (columns) do you think have lots of influence on the sale price of a property?
// what featues (columns) do you think have least influence on the sale price of a property?

// COMMAND ----------

// how many columns
hkPropertiesDF.schema.fields.length

// COMMAND ----------

hkPropertiesDF.count

// COMMAND ----------

// notice missing values for some of the columns
display(hkPropertiesDF)

// COMMAND ----------

// MAGIC %md #### Let's visualize the properties on a map
// MAGIC * Using <a href="https://leafletjs.com/" target="_new">Leaflet</a> javascript library
// MAGIC * Latitude and longitude <a href="https://www.latlong.net/">locator</a>

// COMMAND ----------

val propertyCoordData = hkPropertiesDF.select("Latitude", "Longitude","Prop_Name_ENG").collect
val propertyCoordDataStr = propertyCoordData.map(r => s"""L.circle([${r(0)}, ${r(1)}], {color: 'green',fillColor: '#86FF33',fillOpacity: 0.5,radius: 15}).bindTooltip("${r(2)}").addTo(mymap);""").mkString("\n")


// Fortune building
val trainingBldgCoordStr = """L.circle([22.278055, 114.173454], {color: 'red',fillColor: '#FF0000',fillOpacity: 0.5,radius: 35}).bindTooltip("Fortune Bldg").addTo(mymap);"""


// COMMAND ----------

displayHTML("""
<html>
<head>

 <link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.1/dist/leaflet.css" integrity="sha384-odo87pn1N9OSsaqUCAOYH8ICyVxDZ4wtbGpSYO1oyg6LxyOjDuTeXTrVLuxUtFzv" crossorigin="">
 
 <script src="https://unpkg.com/leaflet@1.3.1/dist/leaflet.js" integrity="sha384-JguaQYjdUVs0XHRhkpHTNjd6j8IjEzgj5+1xeGYHCXQEaufKsYdtn9fgHVeVTLJu" crossorigin="anonymous"></script>
 
 <script src="https://cdnjs.cloudflare.com/ajax/libs/leaflet.heat/0.2.0/leaflet-heat.js"></script>
</head>
<body>
    <div><center><h3>Powered by <a href="https://leafletjs.com/examples/quick-start/" target="_">Leaf Let</a></center></h3></div>
    <div id="mapid" style="width:1100px; height:800px"></div>
  <script>
  var mymap = L.map('mapid').setView([22.289200,114.161817], 15);
  var tiles = L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
}).addTo(mymap); """ + propertyCoordDataStr +  trainingBldgCoordStr + """
  </script>
  </body>
  </html>
""")

// COMMAND ----------

// MAGIC %md #### Exploratory Data Analysis
// MAGIC * Getting to know our data
// MAGIC   * Distribution
// MAGIC   * Correlation
// MAGIC * Data cleaning
// MAGIC   * Remove duplicate rows
// MAGIC   * Missing values
// MAGIC     * Drop rows
// MAGIC     * Replace with default values
// MAGIC * Handling categorical values
// MAGIC * Scaling continuous values
// MAGIC 
// MAGIC (Most Machine Learning algorithms don't accept missing values)

// COMMAND ----------

// any duplicates
hkPropertiesDF.count() - hkPropertiesDF.dropDuplicates().count()

// COMMAND ----------

// generate a few stats for a few important columns
// what do we see?
display(hkPropertiesDF.describe("Bed_Room", "Build_Ages"))

// COMMAND ----------

// what is the most common # of bedrooms among the properties
display(hkPropertiesDF.groupBy("Bed_Room").count)

// COMMAND ----------

// Use DataFrameNaFunctions to fill the rows with missing Bed_Room to the mode (from previous graph)
val hkPropertiesDFWithBR = hkPropertiesDF.na.fill(2, List("Bed_Room"))

// COMMAND ----------

// double checking
display(hkPropertiesDFWithBR.describe("Bed_Room"))

// COMMAND ----------

display(hkPropertiesDFWithBR.groupBy("Bed_Room").count)

// COMMAND ----------

// SaleableArea is a pretty important feature - let's see if there are any missing values
hkPropertiesDFWithBR.describe("Reg_Year", "SaleableArea").show

// COMMAND ----------

// let's drop rows with missing value
// notice the SaleableArea is of String type and has a comma in the value
// so we use regexp_replace to remove the comma and cast it to int
val hkPropertiesWithSaleableArea = hkPropertiesDFWithBR.na.drop(List("SaleableArea"))                                                       
                                                       .withColumn("SaleableArea_Int", regexp_replace($"SaleableArea", ",", "").cast("int"))

// COMMAND ----------

hkPropertiesWithSaleableArea.describe("Reg_Year", "SaleableArea", "SaleableArea_Int", "Rehab_Year").show

// COMMAND ----------

// MAGIC %md #### <a href="https://brplatform.org.hk/en/how-to-rehab" target="_new">Rehabilitation</a>
// MAGIC * When an aged building lacks proper maintenance or is in serious disrepair, comprehensive and large-scale rehabilitation works is necessary to improve  or restore its conditions, facitilites or equipment. Solutions such as maintenance, replacement, improvement and upgrading, coupled with retrofitting measures if applicable, can reinforce the structure of the building and in return, minimise the risk of being declared unsafe.

// COMMAND ----------

// let's fill the rows with missing Rehab_Year with 0
val hkPropertiesWithRehabYear = hkPropertiesWithSaleableArea.na.fill(0, List("Rehab_Year"))

// COMMAND ----------

hkPropertiesWithRehabYear.describe("Reg_Year", "SaleableArea_Int", "Rehab_Year").show

// COMMAND ----------

// MAGIC %md #### Feature Engineering
// MAGIC * Floor column has value from 0 to 53
// MAGIC * Bucketize the column values such that floor (0-15) is "Low", (16-30) is "Medium", (31-53) => "High"
// MAGIC * Why do we want to do this?

// COMMAND ----------

// how do confirm that?
hkPropertiesWithRehabYear.describe("Floor").show

// COMMAND ----------

def floorToSection(floor:Int) : String = {
  floor match {
    case lessThan16 if (lessThan16 < 16) => "L"
    case lessThan31 if (lessThan31 < 31) => "M"
    case lessThan54 if (lessThan54 < 54) => "H"  
  }
}

// COMMAND ----------

floorToSection(50)

// COMMAND ----------

// register our function as a UDF
val floorToSectionUDF = udf(floorToSection(_:Int):String)

// COMMAND ----------

val hkPropertiesWithFloorBucket = hkPropertiesWithRehabYear.withColumn("Floor_Bucket", floorToSectionUDF($"Floor"))

// COMMAND ----------

display(hkPropertiesWithFloorBucket.select("Floor", "Floor_Bucket"))

// COMMAND ----------

display(hkPropertiesWithFloorBucket.groupBy("Floor_Bucket").count)

// COMMAND ----------

hkPropertiesWithFloorBucket.describe("Kindergarten","Primary_Schools","Secondary_Schools").show

// COMMAND ----------

// MAGIC %md #### Combine the # number of schools into a single number

// COMMAND ----------

val hkPropertiesWithSchoolCount = hkPropertiesWithFloorBucket.withColumn("School_count", $"Kindergarten" + $"Primary_Schools" + $"Secondary_Schools")

// COMMAND ----------

display(hkPropertiesWithSchoolCount.select("Kindergarten", "Primary_Schools", "Secondary_Schools", "School_count"))

// COMMAND ----------

/*
|-- Reg_Date: string (nullable = true)
 |-- Reg_Year: integer (nullable = true)
 |-- Prop_Name_ENG: string (nullable = true)
 |-- ADDRESS_ENG: string (nullable = true)
 |-- Prop_Type: string (nullable = true)
 |-- Estate_Size: integer (nullable = true)
 |-- Tower: string (nullable = true)
 |-- Floor: integer (nullable = true)
 |-- Flat: string (nullable = true)
 |-- Bed_Room: integer (nullable = true)
 |-- Roof: string (nullable = true)
 |-- Build_Ages: integer (nullable = true)
 |-- Rehab_Year: integer (nullable = true)
 |-- SalePrice_10k: integer (nullable = true)
 |-- SaleableArea: string (nullable = true)
 |-- Gross Area: string (nullable = true)
 |-- SaleableAreaPrice: string (nullable = true)
 |-- Gross Area_Price: string (nullable = true)
 |-- Kindergarten: integer (nullable = true)
 |-- Primary_Schools: integer (nullable = true)
 |-- Secondary_Schools: integer (nullable = true)
 |-- Parks: integer (nullable = true)
 |-- Library: integer (nullable = true)
 |-- Bus_Route: integer (nullable = true)
 |-- Mall: integer (nullable = true)
 |-- Wet Market: integer (nullable = true)
 |-- Latitude: double (nullable = true)
 |-- Longitude: double (nullable = true)
 */
val featuresToTrain = Array("Bed_Room", "Build_Ages", "Floor_Bucket_Encoding", "SaleableArea_Int", "School_count")


// COMMAND ----------

// MAGIC %md #### Handling Categorical Values
// MAGIC * Some algorithms require features as numeric values only
// MAGIC * Remove the order relationship between the numeric values, otherwise the certain algorithm will take that into consideration
// MAGIC <br />
// MAGIC <img src="https://i.imgur.com/mtimFxh.png" width="400" height="350" />

// COMMAND ----------

// dealing w/ categorical value column by using one-hot-encoding for Floor_Bucket column
val floorStringIndexer = new StringIndexer().setHandleInvalid("skip")
                                                  .setInputCol("Floor_Bucket").setOutputCol("Floor_Bucket_Idx")
val floorBucketEncoder = new OneHotEncoderEstimator().setInputCols(Array("Floor_Bucket_Idx"))
                                                     .setOutputCols(Array("Floor_Bucket_Encoding"))

// COMMAND ----------

// create a column to represent the label column
val featureData = hkPropertiesWithSchoolCount.withColumnRenamed("SalePrice_10k", "label")

// COMMAND ----------

featureData.printSchema

// COMMAND ----------

// MAGIC %md #### Prepare the features to train the model and to perform predictions

// COMMAND ----------

val splits = featureData.randomSplit(Array(0.8, 0.2))
val trainData = splits(0)
val testData = splits(1)

// COMMAND ----------

// MAGIC %md #### <a href="https://spark.apache.org/docs/latest/ml-classification-regression.html">Regression Algorithms</a>
// MAGIC * Linear Regression
// MAGIC * Decision Tree
// MAGIC * Random forest 
// MAGIC * Gradient-boosted tree

// COMMAND ----------

println(s"features: ${featuresToTrain.mkString(", ")}")
val featureAssembler = new VectorAssembler().setInputCols(featuresToTrain).setOutputCol("features")

val linearRegressionAlg = new LinearRegression().setLabelCol("label")
                                                .setMaxIter(10)
                                                .setRegParam(0.3)
                                                .setElasticNetParam(0.8)

val pipeline = new Pipeline().setStages(Array(floorStringIndexer, floorBucketEncoder, featureAssembler, linearRegressionAlg))

// COMMAND ----------

// perform training and predictions
val model = pipeline.fit(trainData)
val predictions = model.transform(testData)

// COMMAND ----------

// examine the prediction price vs the actual price
predictions.select("prediction", "label", "features").show(20, false)

// COMMAND ----------

// MAGIC %md #### Model Evaluation with RMSE
// MAGIC * Using <a href="https://en.wikipedia.org/wiki/Root-mean-square_deviation" target="_new">RSME</a> - root mean square error to measure the difference between predicted and actual ratings
// MAGIC * A measure of standard deviation of the errors the algorithm makes in predictions
// MAGIC * The smaller the better - 0 means perfect
// MAGIC 
// MAGIC <img src="https://wikimedia.org/api/rest_v1/media/math/render/svg/2b68fbc7f91f169aebf70912fea3e8c795e32579" />

// COMMAND ----------

val evaluator = new RegressionEvaluator().setLabelCol("label").setPredictionCol("prediction").setMetricName("rmse") 
val rmse = evaluator.evaluate(predictions)
println(s"Root-mean-square error = $rmse")
// what does the output mean? Is it good or bad?

// COMMAND ----------

val linearRegModel = model.stages(3).asInstanceOf[LinearRegressionModel]

println("β0 (intercept): " + linearRegModel.intercept)

featuresToTrain.zip(linearRegModel.coefficients.toString.replace("[", "").replace("]", "").split(",")).foreach(x => println(s"Coefficeint for $x"))

println("\nR2 score: " + linearRegModel.summary.r2)

// COMMAND ----------

// MAGIC %md #### How to improve the model performance? What do we do next? 

// COMMAND ----------

val decisionTreeAlg = new DecisionTreeRegressor()
  .setLabelCol("label")
  .setFeaturesCol("features")
  .setMaxDepth(10)
  .setMaxBins(30)



/*val gbtAlg = new GBTRegressor()
  .setLabelCol("label")
  .setFeaturesCol("features")
  .setMaxIter(10)
  .setMaxDepth(10)
  .setMaxBins(40)*/

/*val randomForest = new RandomForestRegressor()
  .setLabelCol("label")
  .setFeaturesCol("features")*/

val pipeline2 = new Pipeline().setStages(Array(floorStringIndexer, floorBucketEncoder, featureAssembler, decisionTreeAlg))

// COMMAND ----------

val model2 = pipeline2.fit(trainData)
val predictions2 = model2.transform(testData)

// COMMAND ----------

predictions2.select("prediction", "label", "features").show(20, false)

// COMMAND ----------

val predictions2WithDiff = predictions2.withColumn("diff", $"prediction" - $"label")

// COMMAND ----------

predictions2WithDiff.select("prediction", "label", "diff").orderBy($"diff".desc).show(20, false)

// COMMAND ----------

featureData.filter($"label" === 4508).select("Bed_Room", "Build_Ages", "Floor_Bucket", "SaleableArea_Int", "School_count").show

// COMMAND ----------

predictions2WithDiff.describe("diff").show

// COMMAND ----------

val evaluator2 = new RegressionEvaluator()
  .setLabelCol("label")
  .setPredictionCol("prediction")
  .setMetricName("rmse")
val rmse2 = evaluator2.evaluate(predictions2)
println(s"Root Mean Squared Error (RMSE) rsme = $rmse, rsme2 = $rmse2")

// COMMAND ----------

val gbtModel = model2.stages(3).asInstanceOf[GBTRegressionModel]
println(s"Learned regression GBT model:\n ${gbtModel.toDebugString}")

// COMMAND ----------

val treeModel = model2.stages.last.asInstanceOf[DecisionTreeRegressionModel]
display(treeModel)
