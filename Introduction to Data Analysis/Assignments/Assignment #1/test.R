### Kevin Ip
### DBDA X404
### Assignment 2


# 1. Write a R program to get the first 10 Fibonacci numbers.
#  (What is Fibonnaci Sequence ?
#  The Fibonacci Sequence is the series of numbers:
#    0, 1, 1, 2, 3, 5, 8, 13, 21, 34, ...
#  The next number is found by adding up the two numbers before it:
#    .	the 2 is found by adding the two numbers before it (1+1),
#    .	the 3 is found by adding the two numbers before it (1+2),
#    .	the 5 is (2+3),
#    .	and so on!)

vec = c(0:9)
prev1 = 0
prev2 = 1

count = 1 
for (val in vec) {
  if (count > 2) {
    vec[count] = prev1 + prev2
    prev1 = prev2
    prev2 = vec[count]
    print(vec[count])
  }
  count = count + 1
}

vec


# 2. Read a .CSV File and assign the values to a data frame.
# Select a particular row/column and change the value to NA.
# Using the impute function, replace the value of the row with mean. 

# Read the car data
#setwd("/Users/Kevin/Desktop/DBDA X404/Assignments/Assignment 2")
car_data = read.csv("/Users/Kevin/Desktop/DBDA X404/Assignments/Assignment 2/mtcars.csv")
car_data

# Check whether data structure is data frame, # of rows, # of columns
class(car_data)
nrow(car_data)
ncol(car_data)

# Select Row 7, Column 4(hp) and change it to NA
car_data[7,4] = NA

# Show changed results
car_data[7,]

# Using the impute function, replace the value of the row with mean. 
new_value = mean(car_data[,4], na.rm = TRUE)
new_value
#car_data[7,4] = impute(car_data[7,4], new_value) # Have trouble here
car_data[7,4] = new_value
car_data[7,]

# 3. Write a R program to create three vectors a,b,c with 3 integers.
# Combine the three vectors to become a 3x3 matrix where each column
# represents a vector. Print the content of the matrix.

a = c(1,2,3)
b = c(4,5,6)
c = c(7,8,9)
mat = cbind(a,b,c)
mat

# 4. Write a R program to read the .csv file and display the content
# (You can read any .csv file).

#setwd("/Users/Kevin/Desktop/DBDA X404/Assignments/Assignment 2")
car_data = read.csv("/Users/Kevin/Desktop/DBDA X404/Assignments/Assignment 2/forestfires.csv")
car_data


# 5. Bonus Question:
# Write a R program to create a vector which contains 10 random integer 
# values between -50 and +50.
# Hint: You need to use runif() function for this.

random_ints = runif(10, -50, 50)
random_ints
