package com.androiddevs.shoppinglisttestingyt.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.repositories.ShoppingRepository
import com.androiddevs.shoppinglisttestingyt.utils.Constants
import com.androiddevs.shoppinglisttestingyt.utils.Event
import com.androiddevs.shoppinglisttestingyt.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * Created by karim on 21,November,2020
 */
class ShoppingViewModel @ViewModelInject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val imagesLiveData: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _currentImageURL = MutableLiveData<String>()
    val currentImageURLLiveData: LiveData<String> = _currentImageURL

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatusLiveData: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemStatus


    fun setCurrentImageURL(url: String) {
        _currentImageURL.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemToDatabase(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if(name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()){
            _insertShoppingItemStatus
                .postValue(Event(Resource.error("Fields must not be empty",null)))
            return
        }
        if(name.length > Constants.MAX_NAME_LENGTH){
            _insertShoppingItemStatus.postValue(Event
                (Resource.error("Name is too long, must not " +
                    "exceed ${Constants.MAX_NAME_LENGTH} chars",null)))
            return
        }
        if(priceString.length > Constants.MAX_PRICE_LENGTH){
            _insertShoppingItemStatus.postValue(Event
                (Resource.error("price is too long, must not " +
                    "exceed ${Constants.MAX_PRICE_LENGTH} chars",null)))
            return
        }

        val amount = try {
            amountString.toInt()
        }catch (e:Exception){
            _insertShoppingItemStatus
                .postValue(Event(Resource.error("Please enter a valid amount",null)))
            return
        }

        val shoppingItem = ShoppingItem(name,amount,
            priceString.toFloat(),_currentImageURL.value ?: "")

        insertShoppingItemToDatabase(shoppingItem)

        setCurrentImageURL("")

        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(searchKey: String) {
        if(searchKey.isEmpty())
            return

        _images.value = Event(Resource.loading(null))

        viewModelScope.launch {
            val response = repository.searchForImage(searchKey)
            _images.value = Event(response)
        }
    }

}