// Databricks notebook source
// MAGIC %md ### Vehicle Roaming Score
// MAGIC The purpose of this exercise is to calculate the roamingness score of each vehicle.  This score helps in determining how willing a vehicle is to travel far distance from home.  A low roamingness score suggests that a vehicle tends to travel close to its home location.
// MAGIC 
// MAGIC The romaningness score is computed as such:
// MAGIC * Finding a vehicle home base
// MAGIC    * The latitude and longitude value are in float, consider rounding them to the nearest integer
// MAGIC * Compute the average distance from home
// MAGIC    * Consider using Euclidean distance between coordinates
// MAGIC    * Consider creating a UDF for this purpose

// COMMAND ----------

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

// COMMAND ----------

val dataFile = "/FileStore/tables/roaming/truck_locations-77b55.csv"

// COMMAND ----------

val data = spark.read.option("inferScheme", "true").option("header", "true").csv(dataFile)

// COMMAND ----------

data.printSchema

// COMMAND ----------

// MAGIC %md #### Finding the home base
// MAGIC * The latitude and longtitude value are in float, consider rounding them to the nearest integer
// MAGIC * Group by veh_id, latitude, longitude and perform the count aggregation
// MAGIC * Find the latitude, longitude with the largest count to be the home base

// COMMAND ----------

val data_latlon = data.select('veh_id, 'lat.cast("int"), 'lon.cast("int"))

// COMMAND ----------

display(data_latlon)

// COMMAND ----------

val homebase = data_latlon.groupBy('veh_id, 'lat, 'lon).count()

// COMMAND ----------

display(homebase)

// COMMAND ----------

val orderByCountWindowSpec = Window.partitionBy("veh_id").orderBy($"count".desc)

val homebase_result = homebase.withColumn("ping_count", dense_rank().over(orderByCountWindowSpec))
  .where($"ping_count" <= 1)

// COMMAND ----------

display(homebase_result)

// COMMAND ----------

homebase_result.count

// COMMAND ----------

// MAGIC %md #### Calculate the roamingness
// MAGIC * Join the homebase_result with homebase using veh_id

// COMMAND ----------

val homebase_latlon = homebase_result.select('veh_id, 'lat.as("h_lat"), 'lon.as("h_lon"))

// COMMAND ----------

homebase_latlon.printSchema

// COMMAND ----------

val homebase_join = homebase_latlon.join(homebase, homebase_latlon("veh_id") === homebase("veh_id")) 

// COMMAND ----------

display(homebase_join)

// COMMAND ----------

def euclidieanDistance(lat1:Integer, lon1:Integer, lat2:Integer, lon2:Integer) : Double = { 
   Math.sqrt(Math.pow(lat1-lat2, 2) + Math.pow(lon1-lon2, 2))
 }

// COMMAND ----------

val ucDistanceUDF = udf(euclidieanDistance(_:Integer, _:Integer, _:Integer, _:Integer))

// COMMAND ----------

val homebase_w_distance = homebase_join.withColumn("distance", ucDistanceUDF('h_lat, 'h_lon, 'lat, 'lon))

// COMMAND ----------

display(homebase_w_distance)

// COMMAND ----------

val roaming_score = homebase_w_distance.groupBy(homebase("veh_id")).agg(avg('distance).as("score"))

// COMMAND ----------

display(roaming_score.orderBy('score.desc))

// COMMAND ----------

data.filter('veh_id === 133 || 'veh_id === 132).show
