package com.practicum.playlistmaker.presentation.settings.domain

class SettingsInteractorImpl(
    private val repository: SettingsRepository
) : SettingsInteractor {

    override fun isDarkTheme(): Boolean = repository.isDarkTheme()
    override fun setDarkTheme(isDark: Boolean) = repository.setDarkTheme(isDark)
}