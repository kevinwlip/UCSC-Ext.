// Databricks notebook source
// MAGIC %md ### (Assignment #2) - Scala - (Kevin Ip)

// COMMAND ----------

// MAGIC %md #### 1) Write a function to validate whether parentheses are balanced
// MAGIC 
// MAGIC Expecting true for the following strings
// MAGIC 
// MAGIC <ul>
// MAGIC   <li>(if (zero? x) max (/ 1 x))</li>
// MAGIC   <li>I told him (that it’s not (yet) done). (But he wasn’t listening)</li>
// MAGIC </ul>  
// MAGIC   
// MAGIC Expecting false for the following strings
// MAGIC <ul>
// MAGIC   <li>:-)</li>
// MAGIC   <li>())(</li>
// MAGIC  </ul>
// MAGIC 
// MAGIC 
// MAGIC The following methods are useful for this challenge
// MAGIC chars.isEmpty
// MAGIC chars.head
// MAGIC chars.tail
// MAGIC Hint: you can define an inner function if you need to pass extra parameters to your function.
// MAGIC 
// MAGIC To convert a String to List[Char] ==> "ucsc school".toList
// MAGIC 
// MAGIC Extra credit: write another implementation that uses pattern match with list extraction pattern

// COMMAND ----------

def balance(chars: List[Char]): Boolean = {
    
        var result = 0
        
        for(c <- chars) {
            if(c == ')') {
                result -= 1
                if (result < 0) {
                  return false
                }
            }
            else if (c == '(') {
                result += 1
            }
        }
        result == 0
}
                     
//balance(chars = "(if (zero? x) max (/ 1 x))".toList)
//balance(chars = "I told him (that it’s not (yet) done). (But he wasn’t listening)".toList)
//balance(chars = ":-)".toList)
balance(chars = "())(".toList)

// COMMAND ----------

// MAGIC %md #### 2) Write two functions for performing run length encoding and decoding:
// MAGIC 
// MAGIC <p>encoding("WWWWWWWWWWWWBWWWWWWWWWWWWBBBWWWWWWWW") ==> "12W1B12W3B8W"</p>
// MAGIC <p>decoding("12W1B12W3B8W") ==> "WWWWWWWWWWWWBWWWWWWWWWWWWBBBWWWWWWWW"</p>

// COMMAND ----------

def encoding(input:String) : String = {
    if (input.length == 0) {
        return ""
    }
  
    var result = ""
    var curr_val = input(0)
    var count = 0

    for(i <- 0 to input.length-1) {
        if(curr_val == input(i)) {
            count += 1
        }
        else {
            result += count.toString + input(i-1)
            curr_val = input(i)
            count = 1
        }
    }

    if (input(input.length-1) == input(input.length-2)) {
        result = result + (count.toString + input(input.length-2)) // Check for the last character which may be different
    }
    else {
        result = result + (count.toString + input(input.length-1))
    }
    result

}

encoding("WWWWWWWWWWWWBWWWWWWWWWWWWBBBWWWWWWWW")

// COMMAND ----------

def decoding(input:String) : String = {
    if (input.length == 0) {
        return ""
    }  

    var result = ""
    var curr_digit = ""

    for(ele <- input) {
        if (ele.isDigit == true) {
            curr_digit = curr_digit + ele
        }
        else {
            result = result + (ele.toString * curr_digit.toInt)
            curr_digit = ""
        }
    }
    result.toString
}

decoding("12W1B12W3B8W")

// COMMAND ----------

// MAGIC %md #### 3) Convert decimal value roman numeral

// COMMAND ----------

// decimalToRomanNumeral(1997) => expecting MCMXCVII

// https://www.rapidtables.com/convert/number/roman-numerals-converter.html

// COMMAND ----------

private val romanNumeralMap = Map(1000 -> "M", 900 -> "CM", 500 -> "D", 400 -> "CD", 
                          100 -> "C", 90 -> "XC", 50 -> "L", 40 -> "XL", 
                          10 -> "X", 9 -> "IX", 5 -> "V", 4 -> "IV", 1 -> "I")

private val mapping = for ((k,v) <- romanNumeralMap) yield (v, k) // reversed key and values in romanNumeralMap

private var symbol_priority = Array( "M", "CM", "D", "CD",
                 "C", "XC", "L", "XL",
                 "X", "IX", "V", "IV", "I" )

private var result = ""
private var max_symbols = ""

import scala.collection.mutable.ListBuffer
def decimalToRomanNumeral(num: Int): String = {
  // Always take the maximum possible value from the set of symbols
    var n = num
    while (n != 0) {
        for (symbol <- symbol_priority) {
            if (n >= mapping(symbol)) {
                max_symbols = (n / mapping(symbol).toInt).toString
                result = result + (symbol.toString * max_symbols.toInt)
                n = n - (max_symbols.toInt * mapping(symbol))
            }
        }
    }
    result
}

decimalToRomanNumeral(1997)
