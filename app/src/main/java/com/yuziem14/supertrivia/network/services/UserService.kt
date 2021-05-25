package com.yuziem14.supertrivia.network.services

import com.yuziem14.supertrivia.models.User
import com.yuziem14.supertrivia.models.requests.UserRequest
import com.yuziem14.supertrivia.models.responses.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @GET("auth")
    fun fetch(@Header("Authorization") token: String): Call<User>

    @POST("auth")
    @Headers("Content-Type: application/json")
    fun auth(@Body user: UserRequest): Call<UserResponse>

    @POST("users")
    @Headers("Content-Type: application/json")
    fun register(@Body user: UserRequest): Call<UserResponse>
}