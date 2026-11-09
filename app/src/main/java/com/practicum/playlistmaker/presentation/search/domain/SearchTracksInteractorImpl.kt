package com.practicum.playlistmaker.presentation.search.domain

import com.practicum.playlistmaker.domain.api.TrackRepository

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