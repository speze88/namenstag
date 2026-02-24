package com.speze88.namenstag.domain.matching

/**
 * Implementation of the Kölner Phonetik (Cologne Phonetics) algorithm.
 * This algorithm produces a phonetic code for German names, allowing
 * fuzzy matching of similar-sounding names (e.g., Katharina ~ Catarina).
 */
object ColognePhoneticMatcher {

    fun encode(input: String): String {
        if (input.isBlank()) return ""

        val normalized = input.uppercase()
            .replace("Ä", "AE")
            .replace("Ö", "OE")
            .replace("Ü", "UE")
            .replace("ß", "SS")
            .filter { it.isLetter() }

        if (normalized.isEmpty()) return ""

        val codes = StringBuilder()

        for (i in normalized.indices) {
            val char = normalized[i]
            val prevChar = if (i > 0) normalized[i - 1] else ' '
            val nextChar = if (i < normalized.length - 1) normalized[i + 1] else ' '

            val codeStr = getCode(char, prevChar, nextChar)
            codes.append(codeStr)
        }

        // Remove consecutive duplicates
        val deduplicated = StringBuilder()
        for (i in codes.indices) {
            if (i == 0 || codes[i] != codes[i - 1]) {
                deduplicated.append(codes[i])
            }
        }

        // Remove all zeros except at the beginning
        val result = StringBuilder()
        for (i in deduplicated.indices) {
            if (i == 0 || deduplicated[i] != '0') {
                result.append(deduplicated[i])
            }
        }

        return result.toString()
    }

    private fun getCode(char: Char, prevChar: Char, nextChar: Char): String {
        return when (char) {
            'A', 'E', 'I', 'O', 'U', 'J' -> "0"
            'H' -> ""
            'B' -> "1"
            'P' -> if (nextChar == 'H') "3" else "1"
            'D', 'T' -> if (nextChar in "CSZ") "8" else "2"
            'F', 'V', 'W' -> "3"
            'G', 'K', 'Q' -> "4"
            'X' -> if (prevChar in "CKQ") "8" else "48"
            'L' -> "5"
            'M', 'N' -> "6"
            'R' -> "7"
            'S', 'Z' -> "8"
            'C' -> {
                if (prevChar == ' ' || prevChar !in "AEIOUSZ") {
                    if (nextChar in "AHKLOQRUX") "4" else "8"
                } else {
                    if (nextChar in "AHKOQUX") "4" else "8"
                }
            }
            else -> ""
        }
    }

    fun matches(name1: String, name2: String): Boolean {
        val code1 = encode(name1)
        val code2 = encode(name2)
        return code1.isNotEmpty() && code2.isNotEmpty() && code1 == code2
    }
}
