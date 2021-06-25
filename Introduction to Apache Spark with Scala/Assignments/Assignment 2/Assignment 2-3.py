"""
Assignment 2

3. Function for performing Run-Length Decoding

intToRoman(1997) => expecting MCMXCVII

https://www.rapidtables.com/convert/number/roman-numerals-converter.html
"""

class Solution(object):
    def intToRoman(self, num):
        """
        :type num: int
        :rtype: str
        """

        symbol_map = { "I": 1, "IV": 4, "V": 5, "IX": 9, "X": 10,
                       "XL": 40, "L": 50, "XC": 90, "C": 100,
                       "CD": 400, "D": 500, "CM": 900, "M": 1000 }

        symbol_priority = [ "M", "CM", "D", "CD",
                            "C", "XC", "L", "XL",
                            "X", "IX", "V", "IV", "I"]
        result = ''

        while num != 0:
            for symbol in symbol_priority:
                if num >= symbol_map[symbol]: # We have to use this symbol
                    max_symbols = num // symbol_map[symbol]
                    result += symbol * max_symbols
                    num -= max_symbols * symbol_map[symbol]
        return(result)


a = Solution()
print(a.intToRoman(123))
