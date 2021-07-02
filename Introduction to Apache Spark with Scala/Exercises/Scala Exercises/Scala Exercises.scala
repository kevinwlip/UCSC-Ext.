// Databricks notebook source
// MAGIC %md ### Scala Exercises

// COMMAND ----------

// MAGIC %md #### 1) Working with basic Types

// COMMAND ----------

// mutable variables
var counter:Int = 10
var rating = 0.0
var price = 0.3f



// COMMAND ----------

price = price * 1.5f

// COMMAND ----------

// immutable variables
val msg = "Hello Scala"
println(msg)

// string interpolation
println(s"Greeting $msg")

// weird, but feasible
val ? = scala.math.Pi
println(?)

// COMMAND ----------

// MAGIC %md #### 2) Working with functions

// COMMAND ----------

def hello(name:String) : String = { "Hello " + name }

def hello1() = { "Hi there!" }
def hello2() = "Hi there!"
def hello3 = "Hi there!"

println("calling function hello1: " + hello1)
println("calling function hello1 again: " + hello1())

def max(a:Int, b:Int) : Int = if (a > b) a else b

println("max(4,6) => " + max(4,6))
println("max(8,3) => " + max(8,3))

// COMMAND ----------

// function literals
(x: Int, y: Int) => x + y
val sum = (x: Int, y: Int) => x + y
sum(1,70)
val prod = (x: Int, y: Int) => x * y

def doIt(msg:String, x:Int, y:Int, f: (Int, Int) => Int) = {
  println(msg + f(x,y))
}

doIt("sum (1,80) ==> ", 1, 80, sum)

doIt("prod (2,33) ==> ", 2, 33, prod)

// COMMAND ----------

// MAGIC %md #### 3) Working with tuples

// COMMAND ----------

val pair = ("Scala", 1)
val triplet = (25, "M", pair)

println(pair._1)  
println(pair._2) 

println(s"triplet: (${triplet._1}, ${triplet._2}, (${triplet._3._1}, ${triplet._3._2}))")

// COMMAND ----------

// MAGIC %md #### 4) Working with class

// COMMAND ----------

// constructor with two private instance variables
class Movie1(name:String, year:Int)

// With two getter methods
class Movie2(val name:String, val year:Int)
val m1 = new Movie2("100 days", 2010)
println(s"m1.name ==> ${m1.name}")
println(s"m1.year ==> ${m1.year}")


// With two getter and setter methods
class Movie3(var name:String, var year:Int)
val m2 = new Movie3("100 days", 2010)
m2.name = "100 Hours"

// COMMAND ----------

// MAGIC %md #### 5) Working with case class

// COMMAND ----------

case class Movie4(name:String, year:Int)

val m = Movie4("100 days", 2010)
println("toString => " + m.toString)

println(m.name + " " + m.year)

// COMMAND ----------

// MAGIC %md #### 6) Working with pattern matching

// COMMAND ----------

def errorMsg(n:Int) = n match {
   case 1 => println("Not a problem")
   case 2 => println("You may want to double check")
   case 3 => println("System is shutting down")
}

def range(n:Int) = n match {
  case lessThan10 if (lessThan10 <= 10) => println("0 .. 10")
  case lessThan50 if (lessThan50 <= 50) => println("11 .. 50")
  case _ => println("> 50")
}

println("range(8)  ==> " + range(8))
println("range(25) ==> " + range(25))

// ======================================
//   case matching with Case class
// ======================================

abstract class Shape
case class Rectangle(h:Int, w:Int) extends Shape
case class Circle(r:Int) extends Shape

def area(s:Shape) = s match {
  case Rectangle(h,w) => h * w
  case Circle(r) => r * r * 3.14
}

println("area(Rectangle(4,5)) ==>" + area(Rectangle(4,5)))

println("area(Circle(5)) ==> " + area(Circle(5)))

// COMMAND ----------

// MAGIC %md #### 7) Working with array

// COMMAND ----------

val myArray = Array(1,2,3,4);

println("myArray(0) ==> " + myArray(0))
myArray(0) = myArray(1) + 1;
println("myArray(0) ==> " + myArray(0))                    


myArray.foreach(a => print(a + " "))
println()

println("======== foreach ======")
myArray.foreach(println)

// iterating with index
println("======== iterating with index ======")
for (i <- 0 until myArray.length)
  println(myArray(i))

// iterating without index
println("======== iterating with value only ======")
for (v <- myArray)
  println(v)

def validLength(m:Array[String]) : Boolean =  { 
   if (m.length == 3) true else false 
}

val movie = Array("Lucy", "2014")

validLength(movie)

// COMMAND ----------

// MAGIC %md #### 8) Working with list

// COMMAND ----------

val l = List(1,2,3,4);
l.foreach(println)
l.foreach(x => println(x + " "))

println("l.head ==> " + l.head)
println("l.tail ==> " + l.tail)
println("l.last ==> " + l.last)
println("l.init ==> " + l.init)


val table: List[List[Int]] = List (
       List(1,0,0),
       List(0,1,0),
       List(0,0,1)        
)

val list = List(2,3,4);

// cons operator – prepend a new element to the beginning
val m = 1::list 

// appending
val n = list :+ 5

// to find out whether a list is empty or not
println("empty list? " + m.isEmpty)

// take the first n elements
println("list.take(2) ==> " + list.take(2))

// drop the first n elements
println("list.drop(2) ==> " + list.drop(2))

// COMMAND ----------

// MAGIC %md #### 9) Working with list high order functions

// COMMAND ----------

val n = List(1,2,3,4)
val s = List("LNKD", "GOOG", "AAPL")
val p = List(265.69, 511.78, 108.49)

var product = 1;
n.foreach(product *= _)

println("after filtering ==> " + n.filter(_ % 2 != 0))
println("after partitioning ==> " + n.partition(_ % 2 != 0))

println("finding odd numbers ==> " +n.find(_ % 2 != 0))
println("finding negative numbers ==> " + n.find(_ < 0))

println("take while > 200 ==> " + p.takeWhile(_ > 200.00))
println("drop while > 200 ==> " + p.dropWhile(_ > 200.00))

println("span while > 200 ==> " + p.span(_ > 200.00))

println("====== map and flatMap =======")
println("map ==> " + n.map(_ + 1))
println("flatMap ==> " + s.flatMap(_.toList))

println("====== reduce =======")
println("reduce ==> " + n.reduce((a,b) => (a+b)))
println("reduce with addition ==> " +n.reduce(_ + _))

println("contains(3) ==> " + n.contains(3))

// COMMAND ----------

// MAGIC %md #### 10) Working with list pattern matching

// COMMAND ----------

val n2 = List(1,2,3,4)

// sum using recursion and list with head and tail
def sum(xs: List[Int]) : Int = xs match {
  case Nil => 0
  case x :: ys => x + sum(ys)
}

println("sum => " + sum(n2))

// COMMAND ----------

// MAGIC %md #### 11) Merging two sorted lists

// COMMAND ----------

def merge(xs:List[Int], ys:List[Int]) : List[Int] = {
    (xs, ys) match {
       case (Nil, ys) => ys
       case (xs, Nil) => xs
       case (x :: xs1, y :: ys1) => 
          if (x < y)  x :: merge(xs1, ys)
          else y :: merge(xs, ys1)
    }
}

// COMMAND ----------

val list1 = List(1,5,7,11)
val list2 = List(2,6,8,12)

println("after merging: " + merge(list2, list1))

// COMMAND ----------

// MAGIC %md ### Exercises

// COMMAND ----------

// MAGIC %md ##### 1) Write a function to calculate the length of a given List using recursion

// COMMAND ----------

def length(xs: List[Int]) : Int = xs match {
    case Nil => 0
    case h::t => 1 + length(t)
}

val xs = List(1,2,3,4)
println(length(xs)) // expect 4

// COMMAND ----------

// MAGIC %md ##### 2) Write a function to reverse the elements in the list

// COMMAND ----------

val xs = List(1,2,3,4)
def reverse(xs: List[Int]) : List[Int] = xs match {
  case Nil => Nil
  case h :: t => reverse(t) ::: List(h)
    
}

println(reverse(xs))   // expect List(4,3,2,1)

// COMMAND ----------

// MAGIC %md ##### 3) Write a function to reduce duplicates
// MAGIC 
// MAGIC Given a list of numbers with duplicate values - write a function to remove the duplicates.
// MAGIC 
// MAGIC Hint - think recursively, leverage pattern matching and declare all the possible cases your function needs to handle.  
// MAGIC 
// MAGIC Use List.contains method and list append operator :+

// COMMAND ----------

val dups = List(1,2,3,4,6,3,2,7,9,4)



def removeDups(xs : List[Int]) : List[Int] = xs match {


}

removeDups(dups).sorted  // expect List(1,2,3,4,6,7,9)


// COMMAND ----------

// MAGIC %md ##### 4) Write a function to reduce consecutive duplicates
// MAGIC 
// MAGIC 
// MAGIC List(1,1,1,1,2,3,3,4,4,5) ==> List(1,2,3,4,5)

// COMMAND ----------

val dups = List(1,1,1,1,2,3,3,4,4,5)

def removeConsecutiveDups(xs : List[Int]) : List[Int] = xs match {


}

removeConsecutiveDups(dups) // expect List(1,2,3,4,5)
