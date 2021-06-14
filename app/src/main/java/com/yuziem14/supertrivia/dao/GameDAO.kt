package com.yuziem14.supertrivia.dao

import com.yuziem14.supertrivia.models.errors.Error
import com.yuziem14.supertrivia.models.errors.FailError
import com.yuziem14.supertrivia.models.responses.GameResponse
import com.yuziem14.supertrivia.network.services.GameService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameDAO {
    private var retrofit: Retrofit
    private var service: GameService

    init {
        this.retrofit = Retrofit
            .Builder()
            .baseUrl("http://192.168.1.78:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        this.service = this.retrofit.create(GameService::class.java)
    }

    fun fetch(
        token: String,
        difficulty: String?,
        categoryId: Long?,
        finished: (response: GameResponse) -> Unit,
        fail: (error: FailError) -> Unit
    ) {
        this.service.fetch(token, difficulty, categoryId).enqueue(object: Callback<GameResponse> {
            override fun onResponse(call: Call<GameResponse>, response: Response<GameResponse>) {
                if(response.isSuccessful) {
                    finished(response.body()!!)
                } else {
                    val error = Error.toPOJO(retrofit, FailError::class.java, response.errorBody()!!)
                    fail(error)
                }
            }

            override fun onFailure(call: Call<GameResponse>, t: Throwable) {}
        })
    }

    fun destroy(token: String, finished: (response: GameResponse) -> Unit, fail: (error: FailError) -> Unit) {
        this.service.destroy(token).enqueue(object: Callback<GameResponse> {
            override fun onResponse(call: Call<GameResponse>, response: Response<GameResponse>) {
                if(response.isSuccessful) {
                    finished(response.body()!!)
                } else {
                    val error = Error.toPOJO(retrofit, FailError::class.java, response.errorBody()!!)
                    fail(error)
                }
            }

            override fun onFailure(call: Call<GameResponse>, t: Throwable) {}
        })
    }
}