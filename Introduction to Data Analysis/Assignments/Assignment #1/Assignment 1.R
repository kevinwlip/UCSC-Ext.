### Kevin Ip
### DBDA X404
### Assignment 1


# 1.	Create a Matrix
# - Use Matrix Value 1:40

# a. Decide the appropriate Columns & Rows
# 5 Rows and 8 Columns

mat = matrix(1:40, 5, 8)

mat


# b. Show the result for a column ( you can choose any column)

mat[,3]


# c. Show the result for a row ( you can choose any row)

mat[3,]


# d. Show the result for a row using Modulo operator(Division) where the reminder == 0

mat[5,] %% 5


# e. Show the result using a column & row combination(Eg, 3rd row, 5th column)

mat[5,5]



# 2. Identify the Data Type

# a. Assign a numeric to a variable

a_var = 3


# b. Find the datatype of the variable

typeof(a_var)


# c. Assign a character to a variable

c_var = "character"


# d. Find the datatype of the variable

typeof(c_var)


# 3. Identity the Mean & Median for the following
# Values (14,5,3,22,-8,9,-3,45,12,43,8,6)

values = c(14,5,3,22,-8,9,-3,45,12,43,8,6)


# A) Find the Mean

print(mean(values))


# B) Apply the trim function using 2 (2 numbers from both ends) and find the mean

print(mean(values, trim = 0.2))
print(sort(values))


# C) Find the Median

print(median(values))


# 4. Find the mode(s) for the following
# Values (3,4,3,5,3,6,7,2,1,2,6,7,8,6)

values = c(3,4,3,5,3,6,7,2,1,2,6,7,8,6)

# Here is another function to get the modes in a given vector:
  
# getModes <- function(x) {
#   uniqx <- unique(x)
#   tab <- tabulate(match(x, uniqx))
#   uniqx[tab == max(tab)]
# }
# getModes(values)

# My Mode Solution

counted = table(values)
modes = names(which(max(counted) == counted))
as.numeric(modes)
