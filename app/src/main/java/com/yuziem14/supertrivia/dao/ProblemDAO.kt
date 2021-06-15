package com.yuziem14.supertrivia.dao

import com.yuziem14.supertrivia.models.errors.Error
import com.yuziem14.supertrivia.models.errors.FailError
import com.yuziem14.supertrivia.models.responses.AnswerResponse
import com.yuziem14.supertrivia.models.responses.ProblemResponse
import com.yuziem14.supertrivia.network.services.ProblemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProblemDAO {
    private var retrofit: Retrofit
    private var service: ProblemService

    init {
        this.retrofit = Retrofit
            .Builder()
            .baseUrl("http://192.168.1.78:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        this.service = this.retrofit.create(ProblemService::class.java)
    }

    fun fetch(
        token: String,
        finished: (response: ProblemResponse) -> Unit,
        fail: (error: FailError) -> Unit
    ) {
        this.service.fetch(token).enqueue(object: Callback<ProblemResponse> {
            override fun onResponse(call: Call<ProblemResponse>, response: Response<ProblemResponse>) {
                if(response.isSuccessful) {
                    finished(response.body()!!)
                } else {
                    val error = Error.toPOJO(retrofit, FailError::class.java, response.errorBody()!!)
                    fail(error)
                }
            }

            override fun onFailure(call: Call<ProblemResponse>, t: Throwable) {}
        })
    }

    fun next(
        token: String,
        finished: (response: ProblemResponse) -> Unit,
        fail: (error: FailError) -> Unit
    ) {
        this.service.next(token).enqueue(object: Callback<ProblemResponse> {
            override fun onResponse(call: Call<ProblemResponse>, response: Response<ProblemResponse>) {
                if(response.isSuccessful) {
                    finished(response.body()!!)
                } else {
                    val error = Error.toPOJO(retrofit, FailError::class.java, response.errorBody()!!)
                    fail(error)
                }
            }

            override fun onFailure(call: Call<ProblemResponse>, t: Throwable) {}
        })
    }

    fun answer(
        token: String,
        answer: Long,
        finished: (response: AnswerResponse) -> Unit,
        fail: (error: FailError) -> Unit
    ) {
        this.service.answer(token, answer).enqueue(object: Callback<AnswerResponse> {
            override fun onResponse(call: Call<AnswerResponse>, response: Response<AnswerResponse>) {
                if(response.isSuccessful) {
                    finished(response.body()!!)
                } else {
                    val error = Error.toPOJO(retrofit, FailError::class.java, response.errorBody()!!)
                    fail(error)
                }
            }

            override fun onFailure(call: Call<AnswerResponse>, t: Throwable) {}
        })
    }
}