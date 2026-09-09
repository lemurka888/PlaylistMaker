package com.practicum.playlistmaker.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("search")
    fun searchTracks(
        @Query(value = "term", encoded = true) text: String,  // <-- Добавили encoded = true
        @Query("entity") entity: String = "song"
    ): Call<TrackResponse>
}