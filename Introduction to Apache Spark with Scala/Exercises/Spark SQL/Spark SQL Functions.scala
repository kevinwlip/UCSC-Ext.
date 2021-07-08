// Databricks notebook source
// MAGIC %md ## Spark SQL Functions
// MAGIC 
// MAGIC * <a href="https://spark.apache.org/docs/2.2.0/api/java/index.html?org/apache/spark/sql/functions.html" target="_">Spark functions</a>

// COMMAND ----------

// MAGIC %md ### Playing with Date functions

// COMMAND ----------

import org.apache.spark.sql.functions._

// COMMAND ----------

val data = List(
 (1, "2017-07-31", "2017-07-31 15:04:58.865"),
 (2, "2017-06-11", "2017-07-15 17:24:18.326"),
 (3, "2015-02-19", "2016-05-19 02:10:45.561"))

val dateDF = spark.sparkContext.parallelize(data).toDF("id", "joinDate", "txDate")
dateDF.printSchema

val dateDF2 = dateDF.select($"id", 
              to_date($"joinDate").as("joinDate"), 
              quarter($"joinDate").as("quarter"),
              dayofyear($"joinDate").as("doy"),
              weekofyear($"joinDate").as("woy"),
              last_day($"joinDate").as("lastday_month"),              
              $"txDate",
              datediff(current_timestamp(), $"txDate").as("num_day_ago"))

dateDF2.printSchema

// COMMAND ----------

dateDF2.show

// COMMAND ----------

// MAGIC %md #### Using date function with wikipedia page view data

// COMMAND ----------

val pageviews_by_second = "/databricks-datasets/wikipedia-datasets/data-001/pageviews/raw"
display(dbutils.fs.ls(pageviews_by_second))

// COMMAND ----------

// read the data in TSV format using CSV reader w/ '\t' as the delimiter
// since the input file is a bit large, therefore this will take some time because it needs to infer the schema
val pageviewsDF = sqlContext.read
   .option("header", "true") 
   .option("inferSchema", "true")
   .option("delimiter", "\t") 
   .csv(pageviews_by_second)

// COMMAND ----------

pageviewsDF.count

// COMMAND ----------

pageviewsDF.printSchema

// COMMAND ----------

pageviewsDF.show(10)

// COMMAND ----------

pageviewsDF.select(dayofyear($"timestamp")).distinct().show()

// COMMAND ----------

// MAGIC %md ## Which day of week has the most number of requests?

// COMMAND ----------

// MAGIC %md #### Format timestamp column to day of week (Mon, Tue, Wed, Thu, Fri, Sat, Sun)

// COMMAND ----------

val pageviewsByDayOfWeekDF = pageviewsDF.groupBy(date_format(($"timestamp"), "E").alias("Day of week")).sum()

// COMMAND ----------

spark.catalog.listTables.collect.foreach(println)

// COMMAND ----------

pageviewsByDayOfWeekDF.createTempView("pageViewDoW")
spark.catalog.cacheTable("pageViewDoW")

// COMMAND ----------

pageviewsByDayOfWeekDF.cache

// COMMAND ----------

display(pageviewsByDayOfWeekDF)

// COMMAND ----------

// MAGIC %md ##### Bring up Spark UI and examine the storage tab

// COMMAND ----------

// MAGIC %md #### Define a function to use as a UDF

// COMMAND ----------

def matchDayOfWeek(day:String): String = {
  day match {
    case "Mon" => "1-Mon"
    case "Tue" => "2-Tue"
    case "Wed" => "3-Wed"
    case "Thu" => "4-Thu"
    case "Fri" => "5-Fri"
    case "Sat" => "6-Sat"
    case "Sun" => "7-Sun"
    case _ => "UNKNOWN"
  }
}

// COMMAND ----------

matchDayOfWeek("Sun")

// COMMAND ----------

// MAGIC %md #### Register the above function as an UDF so can we refer to it later

// COMMAND ----------

val prependNumberUDF = udf(matchDayOfWeek(_: String))

// COMMAND ----------

pageviewsByDayOfWeekDF.select(prependNumberUDF($"Day of week").as("dow")).show()

// COMMAND ----------

display((pageviewsByDayOfWeekDF.withColumnRenamed("sum(requests)", "total requests")
  .select(prependNumberUDF($"Day of week").as("dow"), $"total requests")
  .orderBy("dow")))

// COMMAND ----------

// MAGIC %md #### What is the min date, max date, number of days between max and min date in this date set?

// COMMAND ----------

pageviewsDF.select(min('timestamp), min('timestamp), datediff(max('timestamp), min('timestamp))).explain(true)
