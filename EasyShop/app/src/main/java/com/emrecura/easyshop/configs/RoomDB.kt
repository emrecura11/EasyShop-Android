package com.emrecura.easyshop.configs

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emrecura.easyshop.dao.ProductDao
import com.emrecura.easyshop.models.Product
import com.emrecura.easyshop.utils.Converters

@Database(entities = [Product::class], version = 1)
@TypeConverters(Converters::class)
abstract class RoomDB : RoomDatabase() {

    abstract fun productDao()  : ProductDao

}