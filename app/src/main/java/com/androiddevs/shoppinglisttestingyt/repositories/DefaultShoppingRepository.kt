package com.androiddevs.shoppinglisttestingyt.repositories

import androidx.lifecycle.LiveData
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingDAO
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.WebService
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.utils.Resource
import com.bumptech.glide.load.ResourceEncoder
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by karim on 21,November,2020
 */

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDAO: ShoppingDAO,
    private val webService: WebService
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDAO.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDAO.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDAO.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDAO.observeTotalPrice()
    }

    override suspend fun searchForImage(searchKey: String): Resource<ImageResponse> {
        return try {
            val response = webService.searchForImage(searchKey)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("unknown error!", null)
            } else {
                Resource.error("unknown error!", null)
            }
        } catch (e: Exception) {
            return Resource.error("Couldn't reach the server, Check network!", null)
        }
    }

}