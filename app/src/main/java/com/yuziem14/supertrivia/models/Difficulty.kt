package com.yuziem14.supertrivia.models

data class Difficulty(
    var name: String,
    var slug: String
) {
    override fun toString() = this.name
}
