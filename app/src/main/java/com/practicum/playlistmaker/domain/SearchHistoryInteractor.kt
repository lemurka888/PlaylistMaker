package com.practicum.playlistmaker.domain

import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun getHistory(): List<Track>
    fun addTrack(track: Track)
    fun clearHistory()
}