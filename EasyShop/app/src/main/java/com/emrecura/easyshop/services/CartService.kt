package com.emrecura.easyshop.services

import com.emrecura.easyshop.models.AddCartRequest
import com.emrecura.easyshop.models.Cart
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CartService {

    @GET("carts/user/{userId}")
    fun getUserCarts(
        @Path("userId") userId: Long,
        @Header("Authorization") token: String
    ): Call<Cart>


    @POST("carts/add")
    fun addCart(
        @Body addCartRequest: AddCartRequest
    ): Call<Cart>
}