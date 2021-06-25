"""
Assignment 2

2a. Function for performing Run-Length Encoding

Ex. encoding("WWWWWWWWWWWWBWWWWWWWWWWWWBBBWWWWWWWW") ==> "12W1B12W3B8W"
"""

class Solution(object):
    def test(self, input):
        """

        """
        if len(input) == 0:
            return ""

        result = ''
        curr_val = input[0]
        count = 0

        for i in range(len(input)):
            if curr_val == input[i]:
                count += 1
            else:
                result += str(count) + input[i-1]
                curr_val = input[i]
                count = 1

        if input[i] == input[i-1]:   # Check for the last character which may be different
            result += str(count) + input[i-1]
        else:
            result += str(count) + input[i]
        print(result)

a = Solution()
print(a.test("WWWWWWWWWWWWBWWWWWWWWWWWWBBBWWWWWWWW"))
