package com.practicum.playlistmaker.presentation.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.presentation.settings.domain.SettingsInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val _isDarkTheme = MutableLiveData<Boolean>()
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    init {
        _isDarkTheme.value = settingsInteractor.isDarkTheme()
    }

    fun toggleTheme(isDark: Boolean) {
        settingsInteractor.setDarkTheme(isDark)
        _isDarkTheme.value = isDark
    }
}