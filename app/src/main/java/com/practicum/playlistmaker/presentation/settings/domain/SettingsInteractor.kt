package com.practicum.playlistmaker.presentation.settings.domain

interface SettingsInteractor {
    fun isDarkTheme(): Boolean
    fun setDarkTheme(isDark: Boolean)
}