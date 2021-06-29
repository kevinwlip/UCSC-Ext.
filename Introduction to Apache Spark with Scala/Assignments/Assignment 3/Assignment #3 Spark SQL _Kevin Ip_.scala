// Databricks notebook source
// MAGIC %md ### (Assignment #3) - Spark SQL - (Kevin Ip)

// COMMAND ----------

import  org.apache.spark.sql.functions._

// COMMAND ----------

val businesses_plus_file = spark.read.option("header", "false")
    .option("sep", "\t")
    .csv("/FileStore/tables/sf-food/businesses_plus.tsv")
val inspections_plus_file = spark.read.option("header","false")
    .option("sep", "\t")
    .csv("/FileStore/tables/sf-food/inspections_plus.tsv")
val violations_plus_file = spark.read.option("header","false")
    .option("sep", "\t")
    .csv("/FileStore/tables/sf-food/violations_plus.tsv")

// COMMAND ----------

val businesses_df = businesses_plus_file.na.fill("")
val inspections_df = inspections_plus_file.na.fill("")   // Doesn't seeem to work so well with the '.cast(int)' , used '.filter($"score".isNotNull)' which removed the 'null' values in the 'score' column in Question 3.
val violations_df = violations_plus_file.na.fill("")

// COMMAND ----------

val businesses = businesses_df.select(
    businesses_df("_c0").as("business_id"),
    businesses_df("_c1").as("name"),
    businesses_df("_c2").as("address"),
    businesses_df("_c3").as("city"),
    businesses_df("_c4").as("postal_code"),
    businesses_df("_c5").as("latitude"),
    businesses_df("_c6").as("longitude"),
    businesses_df("_c7").as("phone_number"),
    businesses_df("_c8").as("tax_code"),
    businesses_df("_c9").as("business_certificate"),
    businesses_df("_c10").as("application_date"),
    businesses_df("_c11").as("owner_name"),
    businesses_df("_c12").as("owner_address"),
    businesses_df("_c13").as("owner_city"),
    businesses_df("_c14").as("owner_state"),
    businesses_df("_c15").as("owner_zip")
    )

//businesses.printSchema

display(businesses)

// COMMAND ----------

val inspections = inspections_df.select(
    inspections_df("_c0").as("business_id"),
    inspections_df("_c1").as("score").cast("int"),
    inspections_df("_c2").as("date"),
    inspections_df("_c3").as("type")
    )

display(inspections.filter($"score".isNotNull))

// COMMAND ----------

val violations = violations_df.select(
    violations_df("_c0").as("business_id"),
    violations_df("_c1").as("date"),
    violations_df("_c2").as("violationTypeID"),
    violations_df("_c3").as("risk_category"),
    violations_df("_c4").as("description")
    )

violations.printSchema

display(violations)

// COMMAND ----------

// MAGIC %md ##### 1. What is the inspection score distribution like? (inspections_plus.csv)
// MAGIC * Count score frequency - how many occurrences of score 100, 99, 98, etc.

// COMMAND ----------

val score_freq = inspections.groupBy("score").count().orderBy($"score".desc)

display(score_freq)

// COMMAND ----------

// MAGIC %md ##### 2. What is the risk category distribution like? (violations_plus.csv)

// COMMAND ----------

val risk_category_freq = violations.groupBy("risk_category").count().orderBy($"count".desc)

display(risk_category_freq)

// COMMAND ----------

// MAGIC %md ##### 3. Which 20 businesses got lowest scores? (inspections_plus.csv, businesses_plus.csv)
// MAGIC * "business_id","name","address","city","postal_code","latitude",”longitude”, "score"
// MAGIC 
// MAGIC // I'm assuming multiple businesses can have the same lowest scores, re-running gives me different orderings of the businesses

// COMMAND ----------

val businesses_columns = businesses.select(
    businesses("business_id"),
    businesses("name"),
    businesses("address"),
    businesses("city"),
    businesses("postal_code"),
    businesses("latitude"),
    businesses("longitude")
    )

val inspections_columns = inspections.select(
    inspections("business_id"),
    inspections("score")
    )

val businesses_inspections_scores = businesses_columns.join(inspections_columns, "business_id")

display(businesses_inspections_scores.distinct().filter($"score".isNotNull).orderBy($"score").limit(20))

// COMMAND ----------

// MAGIC %md ##### 4. Which 20 businesses got highest scores? (inspections_plus.csv, businesses_plus.csv)
// MAGIC * "business_id","name","address","city","postal_code","latitude",”longitude”, "score"
// MAGIC 
// MAGIC // I'm assuming multiple businesses can have the same highest score - 100 in this case, re-running gives me different orderings of the businesses

// COMMAND ----------

display(businesses_inspections_scores.distinct().orderBy($"score".desc).limit(20))

// COMMAND ----------

// MAGIC %md ##### 5. Among all the restaurants that got 100 score, what kind of high-risk violations did they get (if any)?
// MAGIC * (inspections_plus.csv, violations_plus.csv)

// COMMAND ----------

val violations_columns = violations.select(
    violations("business_id"),
    violations("date"),
    violations("violationTypeID"),
    violations("risk_category"),
    violations("description")
    )

val inspections_columns = inspections.select(
    inspections("business_id"),
    inspections("score")
    )

val violations_inspections_scores = violations_columns.join(inspections_columns, "business_id")

display(violations_inspections_scores.distinct().filter($"score" === 100 && $"risk_category" === "High Risk").orderBy($"violationTypeID"))

// COMMAND ----------

// MAGIC %md ##### 6. Average inspection score by zip code
// MAGIC * (inspections_plus.csv, businesses_plus.csv)

// COMMAND ----------

val businesses_columns = businesses.select(
    businesses("business_id"),
    businesses("name"),
    businesses("address"),
    businesses("city"),
    businesses("postal_code").as("zip_code"),
    businesses("latitude"),
    businesses("longitude")
    )

val inspections_columns = inspections.select(
    inspections("business_id"),
    inspections("score")
    )

val businesses_inspections_scores = businesses_columns.join(inspections_columns, "business_id")

val businesses_inspections_avg_scores = businesses_inspections_scores.select($"zip_code", $"score").groupBy($"zip_code").avg("score")

display(businesses_inspections_avg_scores.filter(length($"zip_code") === 5).orderBy($"avg(score)".desc))

// COMMAND ----------

// MAGIC %md ##### 7. Compute the proportion of all businesses in each neighborhood (zip code) that have incurred at least one of the violations on this list
// MAGIC * "High risk vermin infestation"
// MAGIC * "Moderate risk vermin infestation"
// MAGIC * "Sewage or wastewater contamination”
// MAGIC * "Improper food labeling or menu misrepresentation"
// MAGIC * "Contaminated or adulterated food”
// MAGIC * "Reservice of previously served foods"

// COMMAND ----------

val businesses_columns = businesses.select(
    businesses("business_id"),
    businesses("name"),
    businesses("address"),
    businesses("city"),
    businesses("postal_code").as("zip_code"),
    businesses("latitude"),
    businesses("longitude")
    )

val violations_columns = violations.select(
    violations("business_id"),
    violations("date"),
    violations("violationTypeID"),
    violations("risk_category"),
    violations("description")
    )

val total_violations_in_zip_code = businesses_columns.join(violations_columns, "business_id")
    .groupBy($"zip_code")
    .count()
    .withColumnRenamed("count", "total")

val specific_violations_in_zip_code = businesses_columns.join(violations_columns, "business_id").filter(
    $"description" === "High risk vermin infestation" || 
    $"description" === "Moderate risk vermin infestation" || 
    $"description" === "Sewage or wastewater contamination" || 
    $"description" === "Improper food labeling or menu misrepresentation" || 
    $"description" === "Contaminated or adulterated food" || 
    $"description" === "Reservice of previously served foods")
    .groupBy($"zip_code")
    .count()

val specific_and_total_violations_in_zip_code = specific_violations_in_zip_code.join(total_violations_in_zip_code, "zip_code")

val proportion_of_violations_in_businesses_in_neighborhood = specific_and_total_violations_in_zip_code.select($"zip_code", (($"count" / $"total")*100).alias("percentage_of_businesses_with_at_least_1_violation"))

display(proportion_of_violations_in_businesses_in_neighborhood.filter(length($"zip_code") === 5).orderBy($"percentage_of_businesses_with_at_least_1_violation".desc))

// COMMAND ----------

// MAGIC %md ##### 8. Are SF restaurants clean? Justify your answer

// COMMAND ----------

// MAGIC %md ###### We are analyzing data for restaurants in San Francisco between the middle of 2012 to the middle of 2015. From Question 1, regarding the Inspection Score Distribution we can see that around three-thousand restaurants got scores of 100. As we go down the list we see that the score and the number of restaurants that earned these scores decrease gradually. However, there are also a lot of 'null' value scores around 14,000, these 'null' scores should be very important for our analysisand we should find out why the inspectors gave out 'null' scores. From the Question 2, we see that there are tens of thousands of violations handed out over the three years. Question 3 and Question 4 look for the outliers of the data, the worse scoring restaurants and the best scoring restaurants, respectively. From this data we can see that there are more higher scoring restaurants than poorer scoring restaurants, this deduction is also observed in Question 1. In Question 5 we see that a lot of top scoring restaurants have high risk violations, any violation should deduct points from a restaurant's score, however we do not observe this as these restaurants got top scores of 100. Question 6 is probably the question that is most important for answering if San Francisco restaurants are clean or not. On average, we see that the restaurants in the neighborhoods of San Francisco have around a score of 87 or higher, this score is about a B+ in typical grading standards and that is good news. The last question, Question 7 seems to corroborate that San Francisco has clean restaurants as we see that the percentage of businesses with certain violations is low, less than 10% of restaurants have these violations. 

// COMMAND ----------

// MAGIC %md ###### By analyzing the data from these questions, we can come to a conclusion that this data supplied is not high quality but that most of San Francisco's restaurants are clean. The questions asked and the answers deduced or not deduced seem to point to the conclusion that more analysis needs to be done. We see that we have about 14,000 'null' scores which could impact our analysis greatly. We also see that many top scoring restaurants still have violations which should drop their scores, but these violations do not always drop restaurant scores. Another issue we have to look into are that the inspectors might have been trained differently or have different opinions on certain restaurants and this could lead to confounded data as well. A further point that we should look at is to rephrase certain questions to provide a more thorough analysis. For example, Question 7 could be expanded to ponder about all the health violations instead of certain health violations. Although we see that there are many violations are given out to San Francisco restaurants in Question 2, we see the restaurants score pretty well and are given decent scores throughout the neighborhoods by inspectors in Question 6. Therefore, if most restaurants are clean enough for the inspectors to dine in and score the restaurants well, most of these restaurants are clean enough for citizens to dine in.
