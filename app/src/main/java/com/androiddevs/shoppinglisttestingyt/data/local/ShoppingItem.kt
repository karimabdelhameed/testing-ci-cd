package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by karim on 20,November,2020
 */
@Entity(tableName = "shopping_items")
data class ShoppingItem(
    var name : String  ,
    var amount : Int ,
    var price : Float ,
    var imageURL : String,
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
)

