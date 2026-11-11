package com.practicum.playlistmaker.presentation.search.domain

import com.practicum.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : SearchHistoryInteractor {

    override fun getHistory(): List<Track> = repository.getHistory()
    override fun addTrack(track: Track) = repository.addTrack(track)
    override fun clearHistory() = repository.clearHistory()
}