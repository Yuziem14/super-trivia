package com.yuziem14.supertrivia.models.responses

import com.yuziem14.supertrivia.models.User

data class UserResponse(
    val status: String,
    val data: Data
) {
    data class Data(
        val user: User
    )
}