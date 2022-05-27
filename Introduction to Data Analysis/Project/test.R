

### Poisson Distribution ###
# https://stats.stackexchange.com/questions/286003/what-to-do-when-count-data-does-not-fit-a-poisson-distribution

users <- c(1, 2, 2, 1, 1, 1, 1, 2, 2, 3, 2, 2, 2, 1, 1, 1, 2, 2, 1, 1, 4, 3, 3, 3, 1,
           2, 1, 1, 2, 4, 3, 2, 2, 1, 2, 3, 2, 2, 1, 1, 1, 2, 2, 1, 1, 1, 2, 2, 1, 3,
           2, 1, 2, 3, 2, 1, 2, 1, 2, 1, 1, 3, 1, 1, 1, 2, 2, 2, 3, 1, 2, 1, 2, 4, 4,
           3, 2, 2, 3, 4, 3, 3, 3, 1, 2, 4, 2, 3, 3, 2, 4, 3, 1, 2, 4, 1, 2, 2, 2, 1,
           1, 1, 2, 3, 2, 4, 5, 2, 2, 4, 2, 2, 3, 3, 3, 2, 2, 3, 1, 3, 1, 1, 1, 2, 3,
           6, 3, 3, 4, 2, 2, 2, 3, 1, 1, 1, 2, 2, 3, 2, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2,
           3, 3, 3, 1, 1, 2, 1, 2, 2, 2, 2, 2, 2, 4, 3, 3, 2, 1, 2, 4, 1, 2, 1, 2, 2,
           2, 3, 2, 2, 2, 2, 2, 3, 2, 2, 1, 1, 3, 1, 2, 1, 2, 3, 4, 2, 4, 3, 2, 2, 1,
           4, 2, 2, 1, 1, 2, 2, 2, 1, 1, 1, 2, 2, 3, 3, 1, 1, 2, 1, 2, 1, 3, 3, 3, 3,
           4, 6, 6, 5, 5, 2, 2, 3, 3, 3, 2, 3, 3, 4, 2, 3, 1, 3, 3, 1, 3, 2, 1, 3, 3,
           2, 1, 3, 1, 3, 2, 1, 1, 1, 1, 3, 1, 3, 4, 1, 4, 1, 3, 2, 3, 6, 2, 2, 3, 2,
           1, 2, 2, 2, 2, 2, 1, 2, 3, 2, 2, 4, 2, 2, 2, 3, 2, 2, 5, 3, 2, 2, 3, 2, 2,
           2, 5, 2, 1, 4, 1, 2, 2, 6, 1, 3, 2)

counts <- table(users-1)
mu <- mean(users-1)
expected <- dpois(as.numeric(names(counts)), mu) * length(users)
x <- (counts - expected)^2 / expected
print(round(x, 2)) # Terms in the chi-squared statistic
print(rbind(Expected = round(expected, 0), Actual=counts)) # Compare expected to actual

X <- data.frame(Index=1:length(users), Count=users)
g <- ggplot(X, aes(Index, Count)) + geom_smooth(size=2) + geom_point(size=2, alpha=1/2)
print(g)



https://www.tutorialspoint.com/r/r_logistic_regression.htm

https://stats.stackexchange.com/questions/46692/how-the-na-values-are-treated-in-glm-in-r#:~:text=1%20Answer&text=NA%20Handling%3A%20You%20can%20control,()%20has%20an%20argument%20na.