// Databricks notebook source
val movies_files = "/FileStore/tables/movies/movies.json"
val movie_ratings = "/FileStore/tables/movies/movie_ratings-e9aa9.json"

// COMMAND ----------

dbutils.fs.ls("/FileStore/tables/movies").foreach(println)


// COMMAND ----------

import sqlContext.implicits._ 
import org.apache.spark.sql.functions._

val movies = spark.read.json("/FileStore/tables/movies/movies.json")
movies.printSchema

val movie_ratings = spark.read.json("/FileStore/tables/movies/movie_ratings-e9aa9.json")
movie_ratings.printSchema

// COMMAND ----------

movies.where($"title" === "Coach Carter").show()

// COMMAND ----------

display(movies.where($"title" === "Coach Carter"))

// COMMAND ----------

// MAGIC %md ### 1) Compute how many movies were produced in each year
// MAGIC 
// MAGIC * project out only the title and year column
// MAGIC * perform distinct
// MAGIC * groupby year and perform count aggregation

// COMMAND ----------

val movie_year_df = movies.select("actor", "title", "year").distinct()
val movies_per_year_df = movie_year_df.groupBy("year").count()

// COMMAND ----------

display(movie_year_df.where($"year" === 2000))

// COMMAND ----------

display(movies_per_year_df.sort($"count".desc))

// COMMAND ----------

display(movies_per_year_df.select(min($"year").as("min_year"), max($"year").alias("max_year")))

// COMMAND ----------

// MAGIC %md ### 2) Compute the number of movies each actor had a part in
// MAGIC 
// MAGIC * groupBy actor
// MAGIC * aggregation by count

// COMMAND ----------

 display(movie_year_df.groupBy("actor").count())

// COMMAND ----------

// MAGIC %md ### 3) Which pair of actors worked together the most
// MAGIC 
// MAGIC * Expected output - actor1, actor2, count
// MAGIC * With descending order by count
// MAGIC 
// MAGIC Steps:
// MAGIC * Want to have 2 sets of the movies data
// MAGIC * Rename 'actor' to 'actor1'
// MAGIC * Rename 'actor' to 'actor2'
// MAGIC * Self-join of the movies ('title', 'year')
// MAGIC * actor1, actor2, title, year
// MAGIC * Drop of project the result of the join to contain only actor1, actor2
// MAGIC * Solve the duplicates
// MAGIC   - a1, "Man"   a1, "Man"
// MAGIC   - a2, "Man"   a2, "Man"
// MAGIC   - a1, a1, "Man" (remove)
// MAGIC   - a1, a2, "Man" (remove one of them)
// MAGIC   - a2, a1, "Man"
// MAGIC * groupBy (a1,a2) do the count aggregation

// COMMAND ----------

val movies2 = spark.read.json(movies_files)

// COMMAND ----------

// we can use Column.alias or withColumnRenamed
val moviesWithActor1 = movies.select($"actor".as("actor1"), $"title", $"year")
val moviesWithActor2 = movies2.withColumnRenamed("actor", "actor2")

// COMMAND ----------

val joinedMovies = moviesWithActor1.join(moviesWithActor2, moviesWithActor1("year") === moviesWithActor2("year") && moviesWithActor1("title") === moviesWithActor2("title"))

// COMMAND ----------

val noDupsMovies = joinedMovies.select($"actor1",$"actor2").where($"actor1" > $"actor2")

// COMMAND ----------

val actorWorkedTogether =  noDupsMovies.groupBy($"actor1", $"actor2").count()

// COMMAND ----------

display(actorWorkedTogether.orderBy($"count".desc))

// COMMAND ----------

joinedMovies.select($"actor1",$"actor2", moviesWithActor2("title"), moviesWithActor2("year"), moviesWithActor1("title"), moviesWithActor1("year")).show()

// COMMAND ----------

joinedMovies.select($"actor1",$"actor2", moviesWithActor2("title"), moviesWithActor2("year")).where($"title" === "Freaky Friday"  && ($"actor1" === "McClure, Marc (I)" && $"actor2" === "Lohan, Lindsay") || ($"actor2" === "McClure, Marc (I)" && $"actor1" === "Lohan, Lindsay")).show()

// COMMAND ----------

joinedMovies.select($"actor1",$"actor2", moviesWithActor2("title"), moviesWithActor2("year")).where($"title" === "Freaky Friday"  && ($"actor1" === "McClure, Marc (I)") && ($"actor2" === "McClure, Marc (I)")).show()

// COMMAND ----------

display(actorWorkedTogether.orderBy($"count".desc))

// COMMAND ----------

// MAGIC %md ### 4) Compute the highest rated movies per year
// MAGIC 
// MAGIC * Expected output - year, title, rating, list of actor names
// MAGIC * With descending order by count
// MAGIC 
// MAGIC Steps:
// MAGIC * to find out the highest rated movies per year
// MAGIC * group by year on the ratings dataset
// MAGIC * HINT - use the max function to find the highest rated movie per year
// MAGIC * join the ratings to pickup the title
// MAGIC * join the result of the previous step with movies data set using title and year columns
// MAGIC * group by title, year, and user collect_list on the actor column in the aggregation

// COMMAND ----------

display(movie_ratings)

// COMMAND ----------

// to find out the highest rated movies per year

val hrPerYear =  movie_ratings.groupBy("year").agg(max("rating").as("rating"))


// COMMAND ----------

display(hrPerYear.orderBy("year"))

// COMMAND ----------

val hrMoviePerYear = movie_ratings.join(hrPerYear, (hrPerYear("year") === movie_ratings("year")) && (hrPerYear("rating") ===(movie_ratings("rating")))).select(hrPerYear("year").as("hr_year"), $"title".as("hr_title"), hrPerYear("rating"))

// COMMAND ----------

hrMoviePerYear.count

// COMMAND ----------

val moviesWithHR = movies.join(hrMoviePerYear, $"hr_year" === $"year" && $"hr_title" === $"title").select("rating","actor","title","year")

// COMMAND ----------

display(moviesWithHR)

// COMMAND ----------

val moviesHRWithActor = moviesWithHR.groupBy("rating", "title", "year").agg(collect_list("actor").as("actors"))

// COMMAND ----------

display(moviesHRWithActor)
