package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("search")
    fun searchTracks(
        @Query(value = "term", encoded = true) text: String,
        @Query("entity") entity: String = "song"
    ): Call<TrackResponse>
}