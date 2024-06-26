package com.emrecura.easyshop.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.emrecura.easyshop.models.Product

@Dao
interface ProductDao {
    @Insert
    fun insert(product: Product) : Long

    @Delete
    fun delete(product: Product) : Int

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): List<Product>
}