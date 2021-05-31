package com.yuziem14.supertrivia.dao

import android.util.Log
import com.yuziem14.supertrivia.models.errors.Error
import com.yuziem14.supertrivia.models.errors.FailError
import com.yuziem14.supertrivia.models.responses.CategoryResponse
import com.yuziem14.supertrivia.network.services.CategoryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoryDAO {
    private var retrofit: Retrofit
    private var service: CategoryService

    init {
        this.retrofit = Retrofit
            .Builder()
            .baseUrl("http://192.168.1.78:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        this.service = this.retrofit.create(CategoryService::class.java)
    }

    fun fetch(finished: (response: CategoryResponse) -> Unit, fail: (error: FailError) -> Unit) {
        this.service.fetch().enqueue(object: Callback<CategoryResponse> {
            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>
            ) {
                if(response.isSuccessful) {
                    val categories = response.body()
                    finished(categories!!)
                    return
                }

                val error = Error.toPOJO(retrofit, FailError::class.java, response.errorBody()!!)
                fail(error)
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.e("fail", t.toString())
            }
        })
    }
}