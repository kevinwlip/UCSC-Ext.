// Databricks notebook source
// MAGIC %md ### Spark SQL - Working with nested data (using explode)

// COMMAND ----------

// sample data in JSON format
val jsonString = """[{"name":"LinkedIn", "year":2002, "cities":["Mountain View", "Sunnyvale"], "products":[{"name":"Recruiter", "year":2006}, {"name":"Slideshare", "year":2014}]},
{"name":"Google", "year":1998,  "cities":["Mountain View", "New York", "Santa Monica"], "products":[{"name":"GMail", "year":2004}, {"name":"Android", "year":2008}]},
{"name":"Uber", "year":2009, "cities":["SF", "Chicago"], "products":[{"name":"UberX", "year":2009}, {"name":"UberPool", "year":2014}]}]"""

// COMMAND ----------

import org.apache.spark.sql.functions._

val jsonRDD = spark.sparkContext.parallelize(jsonString :: Nil)

// create data frame using sample data from above
val companies  = spark.read.json(jsonRDD.toDS)
companies.printSchema

// COMMAND ----------

companies.count

// COMMAND ----------

companies.show(false)

// COMMAND ----------

companies.select("name").show
companies.select("cities").show
companies.select("products").show


// COMMAND ----------

val cities = companies.select($"name",explode($"cities").as("city"))
cities.printSchema
cities.show


// COMMAND ----------

val products = companies.select($"name", explode($"products").as("product"))
products.printSchema
products.show


// COMMAND ----------

products.select("name", "product.name", "product.year").show

// COMMAND ----------

// which products from which companies were produced in 2014
products.where($"product.year" === 2014).show
