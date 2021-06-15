package com.yuziem14.supertrivia.models

data class Problem(
    var id: Long? = -1,
    var question: String = "",
    var difficulty: String = "",
    var category: Category? = null,
    var answers: List<Answer> = listOf()
)
