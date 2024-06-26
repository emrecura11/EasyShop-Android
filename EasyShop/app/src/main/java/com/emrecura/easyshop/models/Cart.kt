package com.emrecura.easyshop.models

data class Cart (
    val carts: List<CartElement>,
    val total: Double,
    val skip: Long,
    val limit: Long
)

data class CartElement (
    val id: Long,
    val products: List<CartProduct>,
    val total: Double,
    val discountedTotal: Double,
    val userID: Long,
    val totalProducts: Long,
    val totalQuantity: Long
)
data class CartProduct (
    val id: Long,
    val title: String,
    val price: Double,
    val quantity: Long,
    val total: Double,
    val discountPercentage: Double,
    val discountedTotal: Double,
    val thumbnail: String
)

