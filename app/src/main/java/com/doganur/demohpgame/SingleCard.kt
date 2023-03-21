package com.doganur.demohpgame

data class SingleCard(
    val house: String?,
    val houseScore: Int?,
    val name: String?,
    val score: Int?,
    val image64: String?,
    var faceUp: Boolean = false,
    var isMatched: Boolean = false
)