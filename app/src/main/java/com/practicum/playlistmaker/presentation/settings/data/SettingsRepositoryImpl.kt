package com.practicum.playlistmaker.presentation.settings.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.presentation.settings.domain.SettingsRepository

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    companion object {
        private const val KEY_DARK_THEME = "dark_theme"
    }

    override fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_THEME, false)
    }

    override fun setDarkTheme(isDark: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_DARK_THEME, isDark)
            .apply()
    }
}