'''
0. Download data from https://data.world/fivethirtyeight/hate-crimes. Then read the CSV file original/hate_crimes.csv in to a 
data frame.
1. Create a new column 'non_white_white_poverty_ratio' by finding 
the ratio between the columns: 'share_non_white' and 'share_white_poverty'
2. Draw a scatter plot between 'share_population_in_metro_areas' on the x-axis and 'non_white_white_poverty_ratio' on the y-axis.
3.  Draw a scatter plot between 'median_household_income' on the x-axis and 'non_white_white_poverty_ratio' on the y-axis.
4. Read about Pearson correlation coefficient and then find the correlation between the columns: 'share_population_in_metro_areas', 'median_household_income', and 'non_white_white_poverty_ratio'
'''

import pandas as pd
data = pd.read_csv('hate-crimes/original/hate_crimes.csv')
#print(data.head(5))

#1
data['non_white_white_poverty_ratio'] = data['share_non_white']/data['share_white_poverty']
print(data.columns)

#2 
data.plot.scatter(x = 'share_population_in_metro_areas',
                  y = 'non_white_white_poverty_ratio')

#3 
data.plot.scatter(x = 'median_household_income',
                  y = 'non_white_white_poverty_ratio')

#4
print(data[['share_population_in_metro_areas', 'median_household_income', 'non_white_white_poverty_ratio']].corr('pearson'))




'''
Calculating the value of pi using the monte carlo method.

Read the document in the link: https://academo.org/demos/estimating-pi-monte-carlo/ and calculate the value of pi.

'''
n = 100000
xy = np.random.random(size=(n, 2))*2-1.0
print(xy.max(), xy.min())
x = xy[:,0]
y = xy[:,1]
r = x*x+y*y
print(r[r<1].shape[0])
pi_val = r[r<1].shape[0]*4/n
print(pi_val)