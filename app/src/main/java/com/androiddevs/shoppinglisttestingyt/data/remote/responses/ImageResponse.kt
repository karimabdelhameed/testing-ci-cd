package com.androiddevs.shoppinglisttestingyt.data.remote.responses

/**
 * Created by karim on 21,November,2020
 */

data class ImageResponse(
    val hits: List<ImageResult>,
    val total: Int,
    val totalHits: Int
)