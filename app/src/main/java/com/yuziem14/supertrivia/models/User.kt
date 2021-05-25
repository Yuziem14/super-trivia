package com.yuziem14.supertrivia.models

data class User(
    var name: String,
    var email: String,
    var password: String,
    var token: String? = null
) {
    var id: Long? = null
}
