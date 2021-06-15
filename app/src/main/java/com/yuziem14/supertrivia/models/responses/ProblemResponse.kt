package com.yuziem14.supertrivia.models.responses

import com.yuziem14.supertrivia.models.Problem

data class ProblemResponse(
    var status: String = "",
    var data: Data = Data()
) {
    data class Data(
        var problem: Problem = Problem()
    )
}
