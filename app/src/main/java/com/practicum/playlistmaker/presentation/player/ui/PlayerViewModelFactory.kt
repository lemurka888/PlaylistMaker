package com.practicum.playlistmaker.presentation.player.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.domain.models.Track

class PlayerViewModelFactory(
    private val track: Track
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(track) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}