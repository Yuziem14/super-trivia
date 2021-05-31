package com.yuziem14.supertrivia.models.responses

import com.yuziem14.supertrivia.models.Category

data class CategoryResponse(
    var status: String = "",
    var data: Data = Data()
) {
    data class Data(
        var categories: List<Category> = listOf()
    )
}
