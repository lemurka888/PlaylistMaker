package com.practicum.playlistmaker.presentation.search.domain

import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun getHistory(): List<Track>
    fun addTrack(track: Track)
    fun clearHistory()
}