package com.yuziem14.supertrivia.models.responses

import com.google.gson.annotations.SerializedName
import com.yuziem14.supertrivia.models.Answer

data class AnswerResponse(
    var status: String = "",
    var data: Data = Data()
) {
    data class Data(
        var answer: AnswerData = AnswerData()
    ) {
        data class AnswerData(
            var status: String = "",
            @SerializedName("correct_answer")
            var correctAnswer: Answer = Answer(),
            var score: Long = 0
        )
    }
}