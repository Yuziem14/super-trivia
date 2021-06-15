package com.yuziem14.supertrivia.models.responses

import com.yuziem14.supertrivia.models.RankingPosition

data class RankingResponse(
    var status: String = "",
    var data: Data = Data()
) {
    data class Data(
        var ranking: List<RankingPosition> =  listOf()
    )
}
