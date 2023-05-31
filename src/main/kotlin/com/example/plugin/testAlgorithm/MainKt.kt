package com.example.plugin.testAlgorithm

import org.apache.commons.lang3.StringUtils
import kotlin.math.max


fun main(args: Array<String>) {
    val s1 = "pasword_edit_text"
    val passwordIdList = arrayListOf("password", "pswd")
    val creditCardIdList = arrayListOf("creditCard", "credit", "credit_card")

    var count = 0.0

    //Чтобы вычислить расстояние Яро-Винклера между двумя строками, мы можем использовать StringUtils.getJaroWinklerDistance()
    // метод. Мера Джаро представляет собой взвешенную сумму процента совпадающих символов из каждого файла и
    // транспонированных символов. Винклер увеличил эту меру для соответствия начальным символам.
    for (word in passwordIdList) {
        val maxLength = max(s1.length, word.length)

        println((maxLength - StringUtils.getLevenshteinDistance(s1, word)) / maxLength)

        count = StringUtils.getJaroWinklerDistance(s1, word)
        println(count)

        if (count >= 0.8) {
            println("success: $count")
        }
    }

    val pattern: String = "EditText"
    val text = "\"asjdsajj\" +\n" +
            "                \"asajdasjdsja\" +\n" +
            "                \"asdjsajdasjd\" +\n" +
            "                \"<EditText\" +\n" +
            "                \"id = @id/password\"" +
            "\"asjdsajj\" +\n" +
            "                \"asajdasjdsja\" +\n" +
            "                \"asdjsajdasjd\" +\n" +
            "                \"<EditText\" +\n" +
            "                \"id = @id/password\"" +
            "\"asjdsajj\" +\n" +
            "                \"asajdasjdsja\" +\n" +
            "                \"asdjsajdasjd\" +\n" +
            "                \"<EditText\" +\n" +
            "                \"id = @id/password\""

    val startTime = System.currentTimeMillis()

    val foundIndexes = MainKt().performKMPSearch(text, pattern)

    val endTime = System.currentTimeMillis()

    println(" search time: " + (endTime - startTime).toDouble() + " мс")

    val startTime2 = System.currentTimeMillis()
    var currentIndex = text.indexOf(pattern)
    while (currentIndex != -1) {
        println(currentIndex)
        currentIndex += pattern.length
        currentIndex = text.indexOf(pattern, currentIndex)
    }
    val endTime2 = System.currentTimeMillis()

    println(" search time: " + (endTime2 - startTime2).toDouble() + " мс")


    if (foundIndexes.isEmpty()) {
        System.out.println("Pattern not found in the given text String");
    } else {
        System.out.println("Pattern found in the given text String at positions: " + foundIndexes.toString())
    }
}

class MainKt {
    private fun compilePatternArray(pattern: String): IntArray {
        val patternLength = pattern.length
        var len = 0
        var i = 1
        val compliedPatternArray = IntArray(patternLength)
        compliedPatternArray[0] = 0
        while (i < patternLength) {
            if (pattern[i] == pattern[len]) {
                len++
                compliedPatternArray[i] = len
                i++
            } else {
                if (len != 0) {
                    len = compliedPatternArray[len - 1]
                } else {
                    compliedPatternArray[i] = len
                    i++
                }
            }
        }
        return compliedPatternArray
    }

    fun performKMPSearch(text: String, pattern: String): List<Int> {
        val compliedPatternArray = compilePatternArray(pattern)
        var textIndex = 0
        var patternIndex = 0
        val foundIndexes: MutableList<Int> = ArrayList()
        while (textIndex < text.length) {
            if (pattern[patternIndex] == text[textIndex]) {
                patternIndex++
                textIndex++
            }
            if (patternIndex == pattern.length) {
                foundIndexes.add(textIndex - patternIndex)
                patternIndex = compliedPatternArray[patternIndex - 1]
            } else if (textIndex < text.length && pattern[patternIndex] != text[textIndex]) {
                if (patternIndex != 0) patternIndex = compliedPatternArray[patternIndex - 1] else textIndex += 1
            }
        }
        return foundIndexes
    }
}