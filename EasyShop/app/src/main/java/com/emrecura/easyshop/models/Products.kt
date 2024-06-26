package com.emrecura.easyshop.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Products (
    val products: List<Product>,
    val total: Long,
    val skip: Long,
    val limit: Long
)

@Entity(tableName = "favorites")
data class Product (
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val price: String,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Long,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
): Serializable