package com.practicum.playlistmaker.domain

import com.practicum.playlistmaker.domain.models.Track

interface SearchTracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}