// Databricks notebook source
val sou_folder = "/FileStore/tables/sou"
val stop_words_folder = "/FileStore/tables/stopwords"
val stopWordRDD = spark.sparkContext.textFile(stop_words_folder)


// COMMAND ----------

display(dbutils.fs.ls(sou_folder))
display(dbutils.fs.ls(stop_words_folder))

// COMMAND ----------

// MAGIC %md ###How many words are in all of the state of union speeches?

// COMMAND ----------

val souRDD = spark.sparkContext.textFile(sou_folder)
val words = souRDD.flatMap(line => line.split(" ")).subtract(stopWordRDD).map(w => w.toLowerCase)
words.count()


// COMMAND ----------

// MAGIC %md ### What are the top 20 or 50 commonly used words in all the speeches?

// COMMAND ----------

val wordTuple = words.map(w => (w,1))
val wordCount = wordTuple.reduceByKey(_ + _)

// COMMAND ----------

wordCount.takeOrdered(20)(Ordering[Int].reverse.on(_._2)).foreach(println)

// COMMAND ----------

// MAGIC %md ### Count the number of words that start with each letter in alphabet.

// COMMAND ----------

val firCharTuple = words.map(w => (w(0), 1))
val firCharCount = firCharTuple.reduceByKey(_ + _)
firCharCount.takeOrdered(50).foreach(println)

// COMMAND ----------

// MAGIC %md ### What are some of the longest words in all the speeches?

// COMMAND ----------

val wordLen = words.map(w => (w.length, w)).distinct()

// COMMAND ----------

wordLen.takeOrdered(20)(Ordering[Int].reverse.on(_._1)).foreach(println)

// COMMAND ----------

dbutils.fs.rm("/FileStore/tables/stop_word_list-026c0.txt")
