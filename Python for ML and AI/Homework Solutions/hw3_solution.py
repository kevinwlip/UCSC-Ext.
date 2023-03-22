'''

Create a class called ImageAugmentor which needs to have the following methods
1. rotate() which takes an image and an angle in radians and returns the rotated image.
2. translate() which takes an image and a translation in pixels and returns the translated image. Assume that the translation along x- and y-axis are same.
3. scale() which takes an image and a scaling factor and returns the scaled image.
4. noise() which takes an image, the mean and variance of a Gaussian and applies Gaussian noise returns the noisy image.
5. transform() which takes an image and applies random rotation less than 10 degrees, random translation between -10 and 10 pixels, random scaling between 0.8 and 1.1, random Gaussian noise with a mean of 0 and variance between 0 and 0.02 and return the final image. The transform() method will use methods in #1 to #4 for performing the various operations. You do not need to call methods in #1 to #4 directly.


You need to test your code using the 'dog.png' image supplied in your notes. Finally view the output image. Use the test code below for testing your class.

img = cv2.imread("dog.png")
aug = ImageAugmentor()
img1 = aug.transform(img)
print(img1.shape)
plt.imshow(img1)
plt.show()


'''

import random
import numpy as np
import cv2
from skimage.transform import warp, AffineTransform
from skimage.util import random_noise

import matplotlib.pyplot as plt

class ImageAugmentor:
    def rotate(self, img, ang):
        tform = AffineTransform(rotation=ang)
        out_img = warp(img, tform)
        return out_img
    
    def scale(self, img, scale):
        tform = AffineTransform(scale=scale)
        out_img = warp(img, tform)
        return out_img
    
    def translate(self, img, tr):
        tform = AffineTransform(translation=(tr, tr))
        out_img = warp(img, tform)
        return out_img
    
    def noise(self, img, mean, var):
        return random_noise(img, mean=mean, var=var)
    
    def transform(self, img):
        rand_rot = np.random.random()*np.pi/18.0
        rand_trans = np.random.randint(-10, 10)
        rand_scale = np.random.randint(8, 11)/10.0
        rand_var = np.random.random()*0.02
        print(rand_rot, rand_trans, rand_scale, rand_var)
        img = self.rotate(img, rand_rot)
        img = self.translate(img, rand_trans)
        img = self.scale(img, (rand_scale, rand_scale))        
        img = self.noise(img, 0, rand_var)
        return img
        
img = cv2.imread("dog.png")
aug = ImageAugmentor()
img1 = aug.transform(img)
print(img1.shape)
plt.imshow(img1)
plt.show()