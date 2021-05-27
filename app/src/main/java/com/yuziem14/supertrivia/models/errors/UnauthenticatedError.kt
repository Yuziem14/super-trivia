package com.yuziem14.supertrivia.models.errors

data class UnauthenticatedError(
    var error: String = ""
) : Error()
