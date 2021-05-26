package com.yuziem14.supertrivia.models.errors

data class RegisterError(
    var status: String,
    var data: Data,
) : Error() {
    data class Data(
        val errors: Errors
    ) {
        data class Errors(
            val name: List<String> = listOf(),
            val email: List<String> = listOf(),
            val password: List<String> = listOf()
        )
    }
}
