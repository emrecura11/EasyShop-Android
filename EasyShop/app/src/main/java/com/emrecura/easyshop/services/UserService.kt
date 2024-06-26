package com.emrecura.easyshop.services

import com.emrecura.easyshop.models.Post
import com.emrecura.easyshop.models.User
import com.emrecura.easyshop.models.UserLogin
import com.emrecura.easyshop.models.UserUpdateRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

    @POST("auth/login")
    fun userLogin( @Body userLogin: UserLogin ) : Call<User>

    @GET("users/me")
    fun getUserInfo(
        @Header("Authorization") token: String
    ): Call<User>

    @GET("users/{userId}/posts")
    fun getPosts(
        @Path("userId") userId: Long,
    ):  Call<Post>

    @PUT("users/{id}")
    fun updateUser(
        @Path("id") userId: Long,
        @Body userUpdateRequest: UserUpdateRequest
    ): Call<User>

}