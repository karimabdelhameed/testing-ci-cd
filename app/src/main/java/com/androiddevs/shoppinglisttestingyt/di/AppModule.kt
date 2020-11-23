package com.androiddevs.shoppinglisttestingyt.di

import android.content.Context
import androidx.room.Room
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingDAO
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItemDataBase
import com.androiddevs.shoppinglisttestingyt.data.remote.WebService
import com.androiddevs.shoppinglisttestingyt.repositories.DefaultShoppingRepository
import com.androiddevs.shoppinglisttestingyt.repositories.ShoppingRepository
import com.androiddevs.shoppinglisttestingyt.utils.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by karim on 21,November,2020
 */
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDataBase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ShoppingItemDataBase::class.java, Constants.DATABASE_NAME)
            .build()

    @Singleton
    @Provides
    fun provideShoppingDAO(database: ShoppingItemDataBase) = database.shoppingDAO()

    @Singleton
    @Provides
    fun provideWebServices(): WebService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(WebService::class.java)
    }

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
        dao: ShoppingDAO,
        webService: WebService
    ) = DefaultShoppingRepository(dao, webService) as ShoppingRepository

    @Singleton
    @Provides
    fun provideGlideInstance(@ApplicationContext context: Context) =
        Glide.with(context).apply {
            setDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
            )
        }
}