package com.yuziem14.supertrivia.network.services

import com.yuziem14.supertrivia.models.responses.CategoryResponse
import retrofit2.Call
import retrofit2.http.GET

interface CategoryService {
    @GET("categories")
    fun fetch(): Call<CategoryResponse>
}