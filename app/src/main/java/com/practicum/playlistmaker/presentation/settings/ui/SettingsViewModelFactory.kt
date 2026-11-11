package com.practicum.playlistmaker.presentation.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.presentation.settings.domain.SettingsInteractor

class SettingsViewModelFactory(
    private val settingsInteractor: SettingsInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingsInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}