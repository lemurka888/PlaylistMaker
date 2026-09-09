package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences("theme", MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean("dark_theme", false)


    AppCompatDelegate.setDefaultNightMode(
    if (darkTheme) {
        AppCompatDelegate.MODE_NIGHT_YES
    }else{
        AppCompatDelegate.MODE_NIGHT_NO
    }
    )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            }else{
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        val sharedPrefs = getSharedPreferences("theme", MODE_PRIVATE)
        sharedPrefs.edit()
            .putBoolean("dark_theme", darkThemeEnabled)
            .apply()

    }
}