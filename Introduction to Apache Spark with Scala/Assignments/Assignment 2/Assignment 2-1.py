"""
Assignment 2

1. Write a function to validate whether parentheses are balanced
Expecting true for the following strings

(if (zero? x) max (/ 1 x))
I told him (that it’s not (yet) done). (But he wasn’t listening)
Expecting false for the following strings

:-)
())(
The following methods are useful for this challenge chars.isEmpty chars.head chars.tail Hint: you can define an inner function if you need to pass extra parameters to your function.

To convert a String to List[Char] ==> "ucsc school".toList

Extra credit: write another implementation that uses pattern match with list extraction pattern
"""

class Solution(object):
    def test(self, iter):
        """

        """

        result = 0
        for c in iter:
            if c == ')':
                result -= 1
                if result < 0:
                    return False
            elif c == '(':
                result += 1
        return result == 0

a = Solution()
print(a.test("(())"))
