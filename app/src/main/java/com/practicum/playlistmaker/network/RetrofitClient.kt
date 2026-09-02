package com.practicum.playlistmaker.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val BaseUrl = "https://itunes.apple.com/"

    val api: ITunesApi = Retrofit.Builder()
        .baseUrl(BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ITunesApi::class.java)
}