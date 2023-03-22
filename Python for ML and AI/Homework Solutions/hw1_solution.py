'''
Generate 100 x and y coordinates where y = 3x^3+23x^2+0.25x+2.9 and 
x = [0, 1]. Then store the x and y coordinates to a csv file. 
Ensure that there are only 4 digits after decimal point in the csv file.
'''
import numpy as np
np.set_printoptions(suppress=True)
y = lambda x: 3*x*x*x+23*x*x+0.25*x+2.9
x = np.arange(0, 1, 0.01)
y2 = y(x)
y3 = np.vstack([x.T, y2.T])
np.savetxt('points.csv', y3.T, delimiter=',', fmt='%0.04f')




# locality sensitive hashing
# https://towardsdatascience.com/fast-near-duplicate-image-search-using-locality-sensitive-hashing-d4c16058efcb

'''
Read the document https://towardsdatascience.com/fast-near-duplicate-image-search-using-locality-sensitive-hashing-d4c16058efcb and implement locality sensitive hashing (LSH). You need to test only two hyperplanes x = 0.5 and y = 0.6.Begin by generating random x, y coordinates in the range of [0, 1]. Then test each of the coordinates whether they lie on either side of the hyperplane.
'''
import numpy as np
import matplotlib.pyplot as plt
n = 100
xy = np.random.uniform(size=(n, 2))
print(xy.shape)
plt.plot(xy[:, 0], xy[:, 1], '.')


eq1 = lambda x, y: x-0.5>0
eq2 = lambda x, y: y-0.6>0

eq1_result = eq1(xy[:, 0], xy[:, 1])
eq2_result = eq2(xy[:, 0], xy[:, 1])
print(eq1_result.shape, xy.shape, xy[:,0].shape)
out_matrix = np.stack([xy[:, 0], xy[:, 1], eq1_result, eq2_result], axis=1)
print(out_matrix)
print(eq1_result*2+eq2_result) # group number

plt.axhline(y=0.6, color='black', linestyle='--')
plt.axvline(x=0.5, color='red', linestyle='--')
plt.show()