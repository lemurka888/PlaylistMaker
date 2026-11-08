package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.data.dto.TrackResponse
import com.practicum.playlistmaker.data.network.ITunesApi
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackRepositoryImpl(
    private val api: ITunesApi
) : TrackRepository {

    override fun searchTracks(expression: String, callback: (List<Track>?) -> Unit) {
        api.searchTracks(expression).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.isSuccessful) {
                    val tracks = response.body()?.results?.map { it.toDomain() } ?: emptyList()
                    callback(tracks)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

    private fun TrackDto.toDomain(): Track {
        return Track(
            trackId = trackId,
            trackName = trackName,
            artistName = artistName,
            trackTimeMillis = trackTimeMillis,
            collectionName = collectionName,
            releaseDate = releaseDate,
            primaryGenreName = primaryGenreName,
            country = country,
            artworkUrl100 = artworkUrl100,
            previewUrl = previewUrl
        )
    }
}