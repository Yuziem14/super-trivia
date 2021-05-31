package com.yuziem14.supertrivia.models

data class Category(
    var id: Long,
    var name: String
) {
    companion object {
        const val RANDOM_CATEGORY_ID: Long = -1
    }

    override fun toString() = this.name
}