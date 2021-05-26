package com.yuziem14.supertrivia.dao

import android.util.Log
import com.yuziem14.supertrivia.models.User
import com.yuziem14.supertrivia.models.errors.Error
import com.yuziem14.supertrivia.models.errors.RegisterError
import com.yuziem14.supertrivia.models.requests.UserRequest
import com.yuziem14.supertrivia.models.responses.UserResponse
import com.yuziem14.supertrivia.network.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserDAO {
    private var retrofit: Retrofit
    private var service: UserService

    init {
        this.retrofit = Retrofit
            .Builder()
            .baseUrl("http://192.168.1.78:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        this.service = this.retrofit.create(UserService::class.java)
    }

    fun register(user: User, finished: (response: UserResponse) -> Unit, fail: (errorResponse: RegisterError) -> Unit) {
        val request = UserRequest(user.name, user.email, user.password)
        this.service.register(request).enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if(response.isSuccessful) {
                    val registeredUser = response.body()
                    finished(registeredUser!!)
                    return
                }

                val error = Error.toPOJO(retrofit, RegisterError::class.java, response.errorBody()!!)
                Log.d("errorBody", error.toString())

                fail(error)
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("failed", t.toString())
            }
        })
    }
}