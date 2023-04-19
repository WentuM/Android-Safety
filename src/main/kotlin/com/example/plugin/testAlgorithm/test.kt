package com.example.plugin.testAlgorithm

import org.apache.commons.lang3.StringUtils
import org.xm.Similarity
import org.xm.similarity.text.CosineSimilarity
import kotlin.math.max


fun main(args: Array<String>) {

    val text1 = "pswd"
    val text2 = "psw_edit_text"

    println(Similarity.cilinSimilarity(text1, text2))
    println(Similarity.morphoSimilarity(text1, text2))
    println(CosineSimilarity().getSimilarity(text1, text2))

    val s1 = "pasword_edit_text"
    val passwordIdList = arrayListOf("password", "pswd")
    val creditCardIdList = arrayListOf("creditCard", "credit", "credit_card")

    println(Similarity.phraseSimilarity(text1, text2))

    println(StringUtils.getJaroWinklerDistance(text1, text2))

//    var count = 0.0
//
//    //Чтобы вычислить расстояние Яро-Винклера между двумя строками, мы можем использовать StringUtils.getJaroWinklerDistance()
//    // метод. Мера Джаро представляет собой взвешенную сумму процента совпадающих символов из каждого файла и
//    // транспонированных символов. Винклер увеличил эту меру для соответствия начальным символам.
//    for (word in passwordIdList) {
//        val maxLength = max(s1.length, word.length)
//
//        println((maxLength - StringUtils.getLevenshteinDistance(s1, word)) / maxLength)
//
//        count = StringUtils.getJaroWinklerDistance(s1, word)
//        println(count)
//
//        if (count >= 0.8) {
//            println("success: $count")
//        }
//    }
}