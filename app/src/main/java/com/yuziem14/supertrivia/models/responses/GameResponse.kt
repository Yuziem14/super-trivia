package com.yuziem14.supertrivia.models.responses

import com.yuziem14.supertrivia.models.Game

data class GameResponse(
    var status: String = "",
    var data: Data = Data()
) {
    data class Data(
        var game: Game = Game()
    )
}