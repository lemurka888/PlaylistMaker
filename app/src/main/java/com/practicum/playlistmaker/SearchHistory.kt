package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.data.Track

class SearchHistory(
    private val sharedPreferences: SharedPreferences
) {
    companion object{
        private const val KEY_HISTORY = "history_tracks"
        private const val MAX_HISTORY_SIZE = 10
    }

    private val gson = Gson()

    fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(KEY_HISTORY,null)
        if (json.isNullOrEmpty()) {
            return emptyList()
        }
        val tracksArray = gson.fromJson(json, Array<Track>::class.java)
        return tracksArray?.toList() ?: emptyList()
    }

    fun addTrack(track: Track) {
        val currentHistory = getHistory().toMutableList()

        //поиск трека по trackId
        val indexTrack = currentHistory.indexOfFirst {it.trackId == track.trackId}

        //удалит если найден
        if (indexTrack != -1) {
            currentHistory.removeAt(indexTrack)
        }

        //добавление в начало
        currentHistory.add(0,track)

        //обрезаем список до 10
        if (currentHistory.size > MAX_HISTORY_SIZE) {
            currentHistory.subList(MAX_HISTORY_SIZE, currentHistory.size).clear()
        }

        saveHistory(currentHistory)
    }

    fun clearHistory() {
        sharedPreferences.edit().remove(KEY_HISTORY).apply()
    }

    private fun saveHistory(tracks: List<Track>) {
        val tracksArray = tracks.toTypedArray()
        val json = gson.toJson(tracksArray)
        sharedPreferences.edit()
            .putString(KEY_HISTORY,json)
            .apply()
    }
}