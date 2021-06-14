package com.yuziem14.supertrivia.models

data class Game(
    var status: String = "",
    var creation: String = "",
    var started_at: String = "",
    var finished_at: String = "",
    var score: Long = 0
)
