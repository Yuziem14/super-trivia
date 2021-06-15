package com.yuziem14.supertrivia.network.services

import com.yuziem14.supertrivia.models.responses.RankingResponse
import retrofit2.Call
import retrofit2.http.GET

interface RankingService {
    @GET("/ranking")
    fun fetch(): Call<RankingResponse>
}