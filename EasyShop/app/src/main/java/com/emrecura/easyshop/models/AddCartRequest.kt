package com.emrecura.easyshop.models

data class AddCartRequest(
    val userId: Long,
    val products: List<ProductQuantity>
)

data class ProductQuantity(
    val id: Int,
    val quantity: Int
)
