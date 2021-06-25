"""
Assignment 2

2b. Function for performing Run-Length Decoding

Ex. decoding("12W1B12W3B8W") ==> "WWWWWWWWWWWWBWWWWWWWWWWWWBBBWWWWWWWW"
"""

class Solution(object):
    def test(self, input):
        """

        """
        if len(input) == 0:
            return ""

        result = ''
        curr_digit = ''

        for ele in input:
            if ele.isdigit() == True:
                curr_digit += ele
            else:
                result += ele * int(curr_digit)
                curr_digit = ''

        return result

a = Solution()
print(a.test("12W1B12W3B8W"))
