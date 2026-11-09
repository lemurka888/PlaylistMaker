package com.practicum.playlistmaker.presentation.search.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.search.domain.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SearchHistoryRepository {

    companion object {
        private const val KEY_HISTORY = "history_tracks"
        private const val MAX_HISTORY_SIZE = 10
    }

    private val gson = Gson()

    override fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(KEY_HISTORY, null) ?: return emptyList()
        val tracksArray = gson.fromJson(json, Array<Track>::class.java)
        return tracksArray?.toList() ?: emptyList()
    }

    override fun addTrack(track: Track) {
        val currentHistory = getHistory().toMutableList()
        val indexTrack = currentHistory.indexOfFirst { it.trackId == track.trackId }
        if (indexTrack != -1) {
            currentHistory.removeAt(indexTrack)
        }
        currentHistory.add(0, track)
        if (currentHistory.size > MAX_HISTORY_SIZE) {
            currentHistory.subList(MAX_HISTORY_SIZE, currentHistory.size).clear()
        }
        saveHistory(currentHistory)
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(KEY_HISTORY).apply()
    }

    private fun saveHistory(tracks: List<Track>) {
        val tracksArray = tracks.toTypedArray()
        val json = gson.toJson(tracksArray)
        sharedPreferences.edit()
            .putString(KEY_HISTORY, json)
            .apply()
    }
}