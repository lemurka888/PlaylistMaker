package com.practicum.playlistmaker.presentation.creator

import android.content.Context
import com.practicum.playlistmaker.data.network.ITunesApi
import com.practicum.playlistmaker.data.network.RetrofitClient
import com.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.SearchTracksInteractor
import com.practicum.playlistmaker.domain.SettingsInteractor
import com.practicum.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.impl.SearchTracksInteractorImpl
import com.practicum.playlistmaker.domain.impl.SettingsInteractorImpl

object Creator {

    private const val PREF_NAME = "playlist_maker_pref"

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun provideSearchTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(
            TrackRepositoryImpl(RetrofitClient.api)
        )
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(
            SearchHistoryRepositoryImpl(getPrefs(context))
        )
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(
            SettingsRepositoryImpl(getPrefs(context))
        )
    }
}