package com.example.plugin.testAlgorithm

import org.apache.commons.lang3.StringUtils
import org.xm.Similarity
import org.xm.similarity.text.CosineSimilarity
import kotlin.math.max

fun main(args: Array<String>) {

    val text1 = "passwordEditText"
    val text2 = "password"

    println(Similarity.cilinSimilarity(text1, text2))
    println(Similarity.morphoSimilarity(text1, text2))
    println(CosineSimilarity().getSimilarity(text1, text2))

    val s1 = "pasword_edit_text"
    val passwordIdList = arrayListOf("password", "pswd")
    val creditCardIdList = arrayListOf("creditCard", "credit", "credit_card")

    println(Similarity.phraseSimilarity(text1, text2))

    println(StringUtils.getJaroWinklerDistance(text1, text2))
}