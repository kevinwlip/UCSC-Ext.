// Databricks notebook source
// MAGIC %md #### Binary Classification with Flight Data to predict whether a flight will be delayed or not
// MAGIC * Using <a href="https://spark.apache.org/docs/latest/ml-classification-regression.html">LogistisRegression and DecisionTreeClassifier</a> algorithms
// MAGIC * Data sets: airports.csv, raw_flight_data.parquet
// MAGIC * Schema
// MAGIC <pre>
// MAGIC  |-- DayofMonth: integer (nullable = true)
// MAGIC  |-- DayOfWeek: integer (nullable = true)
// MAGIC  |-- Carrier: string (nullable = true)
// MAGIC  |-- OriginAirportID: integer (nullable = true)
// MAGIC  |-- DestAirportID: integer (nullable = true)
// MAGIC  |-- DepDelay: integer (nullable = true)
// MAGIC  |-- ArrDelay: integer (nullable = true)
// MAGIC </pre>
// MAGIC 
// MAGIC * <a href="https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.package" target="_new">Apache Spark Scala Doc</a>

// COMMAND ----------

import org.apache.spark.sql._
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.ml.classification.BinaryLogisticRegressionSummary
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator

// COMMAND ----------

val dataFolder = "/FileStore/tables/mllib"

// COMMAND ----------

display(dbutils.fs.ls(dataFolder))

// COMMAND ----------

// MAGIC %md #### Utility function to compute confusion matrix metrics

// COMMAND ----------

// a useful function to compute the confusion matrix metrics (TP, FP, TN, FN, Precision, Recall)
def computeConfusionMatrix(predictions:DataFrame, testData:DataFrame) : DataFrame = {
  predictions.cache

  // positive cases
  val tp = predictions.filter($"prediction" === 1 && $"label" === 1).count().toFloat
  val fp = predictions.filter($"prediction" === 1 && $"label" === 0).count().toFloat

  // negative cases
  val tn = predictions.filter($"prediction" === 0 && $"label" === 0).count().toFloat
  val fn = predictions.filter($"prediction" === 0 && $"label" === 1).count().toFloat

  val testSize = testData.count.toFloat
  val cmFetrics = spark.createDataFrame(Seq(
   ("Test Size", testSize),
   ("TP", tp),
   ("FP", fp),
   ("TN", tn),
   ("FN", fn),
   ("Precision", tp / (tp + fp)),
   ("Recall", tp / (tp + fn)))).toDF("metric", "value")

  cmFetrics
}

// COMMAND ----------

// airport information data
val airports = spark.read.option("inferSchema","true").option("header","true").csv(s"$dataFolder/airports.csv")
airports.show()

// COMMAND ----------

// flights data
val flights = spark.read.parquet(s"$dataFolder/raw_flight_data.parquet")

// COMMAND ----------

flights.printSchema

// COMMAND ----------

flights.count

// COMMAND ----------

display(flights)

// COMMAND ----------

// MAGIC %md #### Getting to know the flight data

// COMMAND ----------

display(flights.groupBy("Carrier").count)

// COMMAND ----------

val flightsWithAirport = flights.join(airports, $"OriginAirportID" === $"airport_id")

// COMMAND ----------

val flightsByOriginAirport = flightsWithAirport.groupBy("city").count

// COMMAND ----------

display(flightsByOriginAirport.orderBy($"count".desc))

// COMMAND ----------

// MAGIC %md ### Generate statistics of each column in flights data
// MAGIC * To easily detect missing values
// MAGIC * To understand the distribution
// MAGIC * To understand the outliers

// COMMAND ----------

flights.describe("DepDelay", "ArrDelay").show()
//flights.describe().show()

// COMMAND ----------

flightsWithAirport.filter($"DepDelay" === -63 || $"ArrDelay" === -94).show(false)

// COMMAND ----------

// MAGIC %md #### Cleaning data - assuming the worse
// MAGIC * Identify duplicate rows
// MAGIC * Identify missing values

// COMMAND ----------

// how many duplicate rows?
flights.count() - flights.dropDuplicates().count()

// COMMAND ----------

// the values in columns "ArrDelay", "DepDelay" are important, see if there are any missing values
flights.count() - flights.na.drop("any", Array("ArrDelay", "DepDelay")).count()

// COMMAND ----------

// for the delay time column, 0 means no delay.  We are going to assume there is no delay when the values are missing
val cleanFlightsData = flights.dropDuplicates().na.fill(0, Array("ArrDelay", "DepDelay"))

// COMMAND ----------

cleanFlightsData.cache.count

// COMMAND ----------

// verifying the data is cache
cleanFlightsData.count

// COMMAND ----------

cleanFlightsData.describe().show()

// COMMAND ----------

// MAGIC %md #### Exploring correlation between features
// MAGIC * Correlation value has a range of 0..1  (1 means 100% correlated)

// COMMAND ----------

cleanFlightsData.stat.corr("DepDelay", "ArrDelay")

// COMMAND ----------

// to see the correlation graph between there columns
display(cleanFlightsData)

// COMMAND ----------

// MAGIC %md ### Average delay per each day of week

// COMMAND ----------

display(cleanFlightsData.groupBy($"DayOfWeek").agg{avg("ArrDelay").as("Avg. Arrival Delay")})

// COMMAND ----------

// MAGIC %md ### Arrival delay by airport
// MAGIC * Yours to do :)

// COMMAND ----------

// MAGIC %md ### Prepare data to classify whether a flight will arrive late or not
// MAGIC * Create a label column for each flight (late or not late)
// MAGIC   * <b>Positive</b> means late arrival, <b>negative</b> means no late arrival
// MAGIC * We will consider a flight to be delayed if the arrive delay is greater than 15 minutes

// COMMAND ----------

val featureData = cleanFlightsData.withColumn("label", ($"ArrDelay" > 15).cast("Int"))

// COMMAND ----------

// see the new "label" column
featureData.printSchema

// COMMAND ----------

display(featureData)

// COMMAND ----------

// MAGIC %md #### Distribution of delayed flights vs no delayed flights

// COMMAND ----------

display(featureData.groupBy("label").count)

// COMMAND ----------

// divide the data set into two sets (one for training purpose and the other for model evaluation purpose)
val splits = featureData.randomSplit(Array(0.7, 0.3))
val trainData = splits(0)
val testData = splits(1)

// COMMAND ----------

// MAGIC %md #### Setup a simple Spark ML pipeline
// MAGIC * Stages - features and LogisticRegression algorithm
// MAGIC * What features to use?

// COMMAND ----------

val featuresToTrain = Array("DayofMonth", "DayOfWeek", "OriginAirportID", "DestAirportID", "DepDelay")

// COMMAND ----------

// MAGIC %md #### LogisticRegression Hyperparameters (subset) - tuning knobs (specify, not learned)
// MAGIC * maxIter - number of iterations over the data (default - 100)
// MAGIC * threshold - probability threshold to classify positve or negative class [0.0 - 1.0] (default - 0.5)
// MAGIC * regParam - model complexity [0.0 - 1.0] (default - 0.0) - might overfit when small, might underfit when large
// MAGIC   * Remember overfit and underfit?

// COMMAND ----------

// setup the columns that will be used as features for Logistic Regression algorith to learn from
// since all the columns we are using are numeric type, we don't need to perform much feature engineering
val featureAssembler = new VectorAssembler().setInputCols(featuresToTrain).setOutputCol("features")

val logisticRegAlg = new LogisticRegression().setLabelCol("label")
                                             .setFeaturesCol("features")
                                             .setMaxIter(10)
                                             .setRegParam(0.3)

val pipeline = new Pipeline().setStages(Array(featureAssembler, logisticRegAlg))


// COMMAND ----------

// inspect the parameters in the logisticRegAlg
logisticRegAlg.explainParams

// COMMAND ----------

// MAGIC %md ### Training the model
// MAGIC * This is where the magic happens :)

// COMMAND ----------

// run the training to generate a model
val model = pipeline.fit(trainData)

// COMMAND ----------

val logRegModel = model.stages(1).asInstanceOf[LogisticRegressionModel]

// COMMAND ----------

println(s"Coefficients: ${logRegModel.coefficients} Intercept: ${logRegModel.intercept}")

// COMMAND ----------

// MAGIC %md #### Calculating the area under the curve of ROC
// MAGIC * The ratio of True Positive Rate vs False Postive Rate for every possible classification threshold 
// MAGIC * Value ranges from 0.0 to 1.0 - the higher the value, the better.  
// MAGIC * 0.5 is just as good as random guessing
// MAGIC 
// MAGIC <img src="https://loneharoon.files.wordpress.com/2016/08/screen-shot-2016-08-17-at-16-25-18.png" width="400" height="300"/>

// COMMAND ----------

val trainingSummary = logRegModel.binarySummary

// COMMAND ----------

// this will take a bit of time to calculate the area under the curve
println(s"areaUnderROC: ${trainingSummary.areaUnderROC}")

// COMMAND ----------

// MAGIC %md #### Perform predictions and evaluate model performance
// MAGIC * <a href="https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.ml.evaluation.BinaryClassificationEvaluator">BinaryClassificationEvaluator</a>

// COMMAND ----------

// pointing out the probability value for positive and negative
val predictions = model.transform(testData)
display(predictions.select("features", "probability","prediction", "label"))

// COMMAND ----------

predictions.printSchema

// COMMAND ----------

val evaluator = new BinaryClassificationEvaluator()
                      .setLabelCol("label").setRawPredictionCol("prediction")
                      .setMetricName("areaUnderROC")
val auc = evaluator.evaluate(predictions)
// this AUC is different than above - what is the difference?
println("AUC = " + (auc))

// COMMAND ----------

evaluator.isLargerBetter

// COMMAND ----------

// MAGIC %md #### Calculating Confusing Matrix Metrics
// MAGIC * Learning task is confused about classifying the wrong class
// MAGIC   * <span style="font-size:0.8em">Helpful tip - reading it backward, i.e predicted as (positive|negative), but actually it was (positive|negative)</span>
// MAGIC </br>
// MAGIC <img src="https://www.researchgate.net/profile/Mauno_Vihinen/publication/230614354/figure/fig4/AS:216471646019585@1428622270943/Contingency-matrix-and-measures-calculated-based-on-it-2x2-contigency-table-for.png" width="400" height="300" />
// MAGIC 
// MAGIC <!-- 
// MAGIC <img src="https://cdn-images-1.medium.com/max/1600/1*CPnO_bcdbE8FXTejQiV2dg.png" width="400" height="300" />
// MAGIC -->
// MAGIC 
// MAGIC * Precision = TP / (TP + FP)
// MAGIC * Recall = TP / (TP + FN)
// MAGIC * See below for more details & explanations
// MAGIC   * <a href="https://towardsdatascience.com/beyond-accuracy-precision-and-recall-3da06bea9f6c">Beyond Accuracy</a>
// MAGIC   * <a href="https://www.datasciencecentral.com/profiles/blogs/removing-confusion-from-confusion-matrix-hawaii-false-missile" target="spark">Removing confusion from Confusion Matrix</a>

// COMMAND ----------

display(computeConfusionMatrix(predictions, testData))

// COMMAND ----------

// MAGIC %md #### Discussion about model performance
// MAGIC * Precision is pretty good
// MAGIC * Recall is low - what is the consequence of the FN?

// COMMAND ----------

// MAGIC %md ### Discussion about options of improving model performance
// MAGIC * Change the value of hyperparameters
// MAGIC * Any changes to feature engineering?
// MAGIC * Maybe try a different algorithm

// COMMAND ----------

// MAGIC %md ### Let's try Decision Tree algorithm
// MAGIC * See <a href="https://spark.apache.org/docs/latest/ml-classification-regression.html#decision-tree-classifier" target="_new">Decision tree classifier</a> description and <a href="https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.ml.classification.DecisionTreeClassifier" target="_new">Scala doc.</a> for more info.
// MAGIC * Hyperparameters
// MAGIC   * maxDepth - tree depth, able to capture more data with increasing depth (default - 5)
// MAGIC   * maxBins - number of bins to be created for continuous features, the more bins the greater level of granularity (default - 32)
// MAGIC   * impurity - metric to determine to split at the particular node [entropy, gini] (default - gini)
// MAGIC   
// MAGIC   
// MAGIC <img src="https://static1.squarespace.com/static/59e19ba02278e7ca5b9ffb2b/t/5a07a3bf8165f51550358b35/1510450172549/decisiontree.png" width="400" height="300" />

// COMMAND ----------

import org.apache.spark.ml.classification.{DecisionTreeClassifier, DecisionTreeClassificationModel}
import org.apache.spark.ml.Pipeline

// COMMAND ----------

// setup the columns that will be used as features for Logistic Regression algorith to learn from
// since all the columns we are using are numeric type, we don't need to perform much feature engineering
val featureAssembler = new VectorAssembler().setInputCols(featuresToTrain).setOutputCol("features")

val decisionTreeAlg = new DecisionTreeClassifier().setLabelCol("label")
                                                  .setMaxDepth(8)
                                                  .setMaxBins(30)

val pipeline2 = new Pipeline().setStages(Array(featureAssembler, decisionTreeAlg))


// COMMAND ----------

val model2 = pipeline2.fit(trainData)

// COMMAND ----------

val predictions2 = model2.transform(testData)

// COMMAND ----------

val treeModel = model2.stages.last.asInstanceOf[DecisionTreeClassificationModel]

// COMMAND ----------

// examine the output to gain an understanding of the importance of each feature
treeModel.featureImportances.toArray.zipWithIndex
            .map(_.swap)
            .sortBy(-_._2)
            .foreach(x => println(x._1 + "-" + featuresToTrain(x._1) + " -> " + x._2))

// COMMAND ----------

display(treeModel)

// COMMAND ----------

val evaluator = new BinaryClassificationEvaluator()
                      .setLabelCol("label").setRawPredictionCol("prediction")
                      .setMetricName("areaUnderROC")
val auc = evaluator.evaluate(predictions2)
println("AUC = " + (auc))

// COMMAND ----------

display(computeConfusionMatrix(predictions2, testData))

// COMMAND ----------

// MAGIC %md ##### The End 
