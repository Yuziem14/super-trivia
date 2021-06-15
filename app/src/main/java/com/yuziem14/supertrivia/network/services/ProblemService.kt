package com.yuziem14.supertrivia.network.services

import com.yuziem14.supertrivia.models.responses.AnswerResponse
import com.yuziem14.supertrivia.models.responses.ProblemResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ProblemService {
    @GET("/problems/view")
    fun fetch(
        @Header("Authorization") token: String
    ): Call<ProblemResponse>

    @GET("problems/next")
    fun next(
        @Header("Authorization") token: String
    ): Call<ProblemResponse>

    @POST("problems/answer")
    fun answer(
        @Header("Authorization") token: String,
        @Query("answer") answer: Long,
    ): Call<AnswerResponse>
}