package com.practicum.playlistmaker.network

import com.practicum.playlistmaker.data.Track

data class TrackResponse (
    val results: List<Track> = emptyList()
)