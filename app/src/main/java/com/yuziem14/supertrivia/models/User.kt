package com.yuziem14.supertrivia.models

data class User(
    var name: String,
    var email: String,
    var password: String = "",
    var token: String? = "",
    var id: Long? = null
)
