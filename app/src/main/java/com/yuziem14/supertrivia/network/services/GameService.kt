package com.yuziem14.supertrivia.network.services

import com.yuziem14.supertrivia.models.Difficulty
import com.yuziem14.supertrivia.models.responses.GameResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GameService {
    @GET("/games")
    fun fetch(
        @Header("Authorization") token: String,
        @Query("difficulty") difficulty: String?,
        @Query("category_id") categoryId: Long?
    ): Call<GameResponse>

    @DELETE("/games")
    fun destroy(
        @Header("Authorization") token: String
    ): Call<GameResponse>
}