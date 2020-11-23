package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by karim on 20,November,2020
 */
@Database(
    entities = [ShoppingItem::class],
    version = 1
)
abstract class ShoppingItemDataBase : RoomDatabase() {
    abstract fun shoppingDAO(): ShoppingDAO
}