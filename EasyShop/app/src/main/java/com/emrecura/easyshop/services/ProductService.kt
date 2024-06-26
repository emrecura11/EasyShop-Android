package com.emrecura.easyshop.services

import com.emrecura.easyshop.models.Category
import com.emrecura.easyshop.models.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.emrecura.easyshop.models.Products
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    fun getProducts(@Query("limit") limit: Long): Call<Products>

    @GET("products/search")
    fun searchProducts(@Query("q") query: String): Call<Products>

    @GET("products/categories")
    fun getCategories(): Call<List<Category>>

    @GET("products/category/{categorySlug}")
    fun getProductsByCategory(@Path("categorySlug") categorySlug: String): Call<Products>
}