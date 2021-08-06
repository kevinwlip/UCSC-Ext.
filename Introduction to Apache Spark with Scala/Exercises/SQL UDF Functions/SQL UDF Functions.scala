// Databricks notebook source
// MAGIC %md ## SQL UDF Functions

// COMMAND ----------

case class Student(name:String, score:Int)

// COMMAND ----------

val data = Seq(Student("Joe", 85), 
               Student("Jane", 90), 
               Student("Mary", 55))

// COMMAND ----------

val studentDF = spark.sparkContext.parallelize(data).toDF

// COMMAND ----------

studentDF.printSchema

// COMMAND ----------

// MAGIC %md #### Define Scala function

// COMMAND ----------

def letterGrade(score:Integer) : String = { 
  score match {
    case score if score > 100 => "Cheating"
    case score if score >= 90 => "A"
    case score if score >= 80 => "B"
    case score if score >= 70 => "C"
    case _ => "F"
  }
 }

// COMMAND ----------

// MAGIC %md ### UDF Registration

// COMMAND ----------

import org.apache.spark.sql.functions._

// COMMAND ----------

val letterGradeUDF = udf(letterGrade(_:Integer))

// COMMAND ----------

studentDF.select($"name",$"score",
                 letterGradeUDF($"score").as("grade")).show


// COMMAND ----------

val letterGradeUDF = sqlContext.udf.register("letterGrade", (score: Int) => letterGrade(score))

// COMMAND ----------

spark.sqlContext.udf.register("letterGrade", 
                              letterGrade(_: Int): String)


// COMMAND ----------

studentDF.createOrReplaceTempView("students")

// COMMAND ----------

spark.sql("select name,letterGrade(score) as grade from students").show
