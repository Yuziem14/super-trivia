package com.yuziem14.supertrivia.dao

import com.yuziem14.supertrivia.models.errors.Error
import com.yuziem14.supertrivia.models.errors.FailError
import com.yuziem14.supertrivia.models.responses.RankingResponse
import com.yuziem14.supertrivia.network.services.RankingService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RankingDAO {
    private var retrofit: Retrofit
    private var service: RankingService

    init {
        this.retrofit = Retrofit
            .Builder()
            .baseUrl("https://super-trivia-server.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        this.service = this.retrofit.create(RankingService::class.java)
    }

    fun fetch(
        finished: (response: RankingResponse) -> Unit,
        fail: (error: FailError) -> Unit
    ) {
        this.service.fetch().enqueue(object: Callback<RankingResponse> {
            override fun onResponse(call: Call<RankingResponse>, response: Response<RankingResponse>) {
                if(response.isSuccessful) {
                    finished(response.body()!!)
                } else {
                    val error = Error.toPOJO(retrofit, FailError::class.java, response.errorBody()!!)
                    fail(error)
                }
            }

            override fun onFailure(call: Call<RankingResponse>, t: Throwable) {}
        })
    }
}