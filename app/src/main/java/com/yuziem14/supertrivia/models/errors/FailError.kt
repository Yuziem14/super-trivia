package com.yuziem14.supertrivia.models.errors

data class FailError(
    var status: String = "",
    var message: String = ""
): Error()
