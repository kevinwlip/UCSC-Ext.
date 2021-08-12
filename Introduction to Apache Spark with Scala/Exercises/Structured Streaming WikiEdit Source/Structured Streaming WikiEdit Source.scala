// Databricks notebook source
// MAGIC %md #### Use Structured Streaming to analyze Wikipedia edit events
// MAGIC <p>General Instructions</p>
// MAGIC <ul>
// MAGIC   <li>Make to upload the streaming_sources-assembly-0.0.1.jar as a library</li>
// MAGIC </ul>
// MAGIC 
// MAGIC <p>Github repo - https://github.com/hienluu/structured-streaming-sources</p>

// COMMAND ----------

import org.apache.spark.sql.functions._

// COMMAND ----------

// read stream from custom source

val enWikiEdit = spark.readStream.format("org.structured_streaming_sources.wikedit.WikiEditSourceV2").load
//val enWikiEdit = spark.readStream.format("org.wikiedit.receiver.structured_streaming.WikiEditSourceV2").load


// COMMAND ----------

enWikiEdit.isStreaming

// COMMAND ----------

enWikiEdit.printSchema

// COMMAND ----------

// configure the output mode and write to memory 
val enWikiEditQuery = enWikiEdit.writeStream.outputMode("append").queryName("en_wikiedit").format("memory").start()

// COMMAND ----------

enWikiEditQuery.stop

// COMMAND ----------

display(spark.sql("select * from en_wikiedit"))

// COMMAND ----------

spark.sql("select * from en_wikiedit").count

// COMMAND ----------

enWikiEditQuery.stop

// COMMAND ----------

// MAGIC %md #### Union a few WikiEdit streams and generate a report
// MAGIC 
// MAGIC Follow <a href="https://meta.wikimedia.org/wiki/IRC/Channels" target="new">this</a> link to see a list of available Wikipedia Channels
// MAGIC 
// MAGIC <p>Examples: #en_wikipedia, #es_wikipedia, #ja_wikipedia, #zh_wikipedia</p>

// COMMAND ----------

val customFormat = "org.structured_streaming_sources.wikedit.WikiEditSourceV2"

val enWikiEdit = spark.readStream.format(customFormat)
                                 .load
val esWikiEdit = spark.readStream.format(customFormat)
                                 .option("channel", "#es.wikipedia")
                                 .load

val frWikiEdit = spark.readStream.format(customFormat)
                                 .option("channel", "#fr.wikipedia")
                                 .load
val jaWikiEdit = spark.readStream.format(customFormat)
                                 .option("channel", "#ja.wikipedia")
                                 .load

// COMMAND ----------

frWikiEdit.printSchema

// COMMAND ----------

val allWikiEdit = enWikiEdit.union(esWikiEdit).union(frWikiEdit).union(jaWikiEdit).select("channel").groupBy("channel").agg(count("channel") as "count")

// COMMAND ----------

val allWikiEditQuery = allWikiEdit.writeStream.format("memory").outputMode("complete").queryName("edit_stats").start

// COMMAND ----------

spark.sql("select * from edit_stats").show

// COMMAND ----------

display(spark.sql("select * from edit_stats"))

// COMMAND ----------

allWikiEditQuery.stop

// COMMAND ----------

// MAGIC %md #### Stop all query streams

// COMMAND ----------

for(qs <- spark.streams.active) {
    println(s"Stop streaming query: ${qs.name} - active: ${qs.isActive}")
    if (qs.isActive) {
      qs.stop  
    }
}
