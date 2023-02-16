# Example of apply method
newmovies2['budget'] = newmovies2['budget'].apply(lambda x:x/1000000)
print(newmovies2['budget_millions'])

# Example of combining two or more columns to produce new columns
newmovies2['profit'] = newmovies2['gross']-newmovies2['budget']
print(newmovies2.columns)