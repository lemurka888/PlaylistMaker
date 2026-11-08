package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.SearchTracksInteractor
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Track

class SearchTracksInteractorImpl(
    private val repository: TrackRepository
) : SearchTracksInteractor {

    override fun searchTracks(expression: String, consumer: SearchTracksInteractor.TracksConsumer) {
        repository.searchTracks(expression) { tracks ->
            if (tracks != null) {
                consumer.consume(tracks, null)
            } else {
                consumer.consume(null, "Ошибка поиска")
            }
        }
    }
}