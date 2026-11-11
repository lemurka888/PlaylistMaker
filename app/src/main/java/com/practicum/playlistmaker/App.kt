package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.presentation.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.presentation.creator.Creator

class App: Application() {

    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        Creator.init(this)
        settingsInteractor = Creator.provideSettingsInteractor()
        switchTheme(settingsInteractor.isDarkTheme())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            }else{
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}