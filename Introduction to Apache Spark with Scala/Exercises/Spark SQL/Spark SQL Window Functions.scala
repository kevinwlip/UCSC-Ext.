// Databricks notebook source
// MAGIC %md ### Window Function Examples

// COMMAND ----------

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

// COMMAND ----------

// MAGIC %md #### Pivoting - student weight data

// COMMAND ----------

case class Student(name:String, gender:String, weight:Int, grad_year:Int)
val studentsDF = Seq(Student("John", "M", 180, 2015), 
                     Student("Mary", "F", 110, 2015), 
                     Student("Derek", "M", 200, 2015),
                     Student("Julie", "F", 109, 2015), 
                     Student("Allison", "F", 105, 2015),
                     Student("kirby", "F", 115, 2016), 
                     Student("Jeff", "M", 195, 2016)).toDF 

studentsDF.groupBy("grad_year")
          .pivot("gender")
          .agg(avg("weight")).show()

// COMMAND ----------

studentsDF.show

// COMMAND ----------

// MAGIC %md #### Window Function

// COMMAND ----------

val custSpentData = List(("John", "2017-07-02", 15.35),
                         ("John", "2016-07-04", 27.72),
                         ("John", "2016-07-06", 21.33),
                         ("Mary", "2017-07-01", 59.44),
                         ("Mary", "2017-07-03", 99.76),
                         ("Mary", "2017-07-05", 80.18),
                         ("Mary", "2017-07-07", 69.74))

// COMMAND ----------

 val custSpentDataDF = spark.sparkContext.parallelize(custSpentData).toDF("name", "date", "amount")

 custSpentDataDF.printSchema

 custSpentDataDF.show

// COMMAND ----------

// MAGIC %md ### Top 2 spending amounts
// MAGIC <ul>
// MAGIC   <li><a href="https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.sql.functions$" target="_">Documentation</a> of dense_rank() function</li>
// MAGIC   <li>denseRank leaves no gaps in ranking sequence when there are ties</li>
// MAGIC </ul>

// COMMAND ----------

val orderByAmountWindowSpec = Window.partitionBy("name").orderBy($"amount".desc)

custSpentDataDF.withColumn("topSpent", dense_rank().over(orderByAmountWindowSpec))
               .where($"topSpent" <= 2).show

// COMMAND ----------

// MAGIC %md ### Spending Trend

// COMMAND ----------

val orderByDateWindowSpec = Window.partitionBy("name").orderBy("date")

custSpentDataDF.withColumn("amt_diff", ($"amount" - lag($"amount", 1).over(orderByDateWindowSpec))).show

// COMMAND ----------

// MAGIC %md ### Culmulative Spending

// COMMAND ----------

val orderByDateWithRowsWindowSpec = Window.partitionBy("name").orderBy("date").rowsBetween(Window.unboundedPreceding, Window.currentRow)
custSpentDataDF.withColumn("cul_sum", sum($"amount").over(orderByDateWithRowsWindowSpec)).show

// COMMAND ----------

// MAGIC %md ### Price difference between highest amount and other transactions

// COMMAND ----------

val highestAmountWindowSpec = Window.partitionBy("name").orderBy("date").rowsBetween(Window.unboundedPreceding, Window.unboundedFollowing)
custSpentDataDF.withColumn("amt_diff", max($"amount").over(highestAmountWindowSpec)- $"amount").show
