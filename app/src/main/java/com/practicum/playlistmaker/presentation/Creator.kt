package com.practicum.playlistmaker.presentation.creator

import android.app.Application
import android.content.Context
import com.practicum.playlistmaker.data.network.RetrofitClient
import com.practicum.playlistmaker.presentation.search.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.presentation.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.presentation.search.domain.SearchHistoryInteractor
import com.practicum.playlistmaker.presentation.search.domain.SearchTracksInteractor
import com.practicum.playlistmaker.presentation.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.presentation.search.domain.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.presentation.search.domain.SearchTracksInteractorImpl
import com.practicum.playlistmaker.presentation.settings.domain.SettingsInteractorImpl

object Creator {

    private const val PREF_NAME = "playlist_maker_pref"

    private lateinit var application: Application

    fun init(application: Application) {
        this.application = application
    }

    private fun getPrefs() =
        application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun provideSearchTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(
            TrackRepositoryImpl(RetrofitClient.api)
        )
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(
            SearchHistoryRepositoryImpl(getPrefs())
        )
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(
            SettingsRepositoryImpl(getPrefs())
        )
    }
}