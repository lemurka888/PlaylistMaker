package com.practicum.playlistmaker.presentation.settings.domain

interface SettingsRepository {
    fun isDarkTheme(): Boolean
    fun setDarkTheme(isDark: Boolean)
}