package com.practicum.playlistmaker.presentation.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.search.domain.SearchHistoryInteractor
import com.practicum.playlistmaker.presentation.search.domain.SearchTracksInteractor

class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState> = _searchState

    private val _history = MutableLiveData<List<Track>>()
    val history: LiveData<List<Track>> = _history

    private val _historyVisible = MutableLiveData<Boolean>()
    val historyVisible: LiveData<Boolean> = _historyVisible

    init {
        loadHistory()
    }

    fun search(query: String) {
        _searchState.value = SearchState.Loading

        searchTracksInteractor.searchTracks(query, object : SearchTracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                if (errorMessage != null) {
                    _searchState.postValue(SearchState.Error)
                } else if (foundTracks.isNullOrEmpty()) {
                    _searchState.postValue(SearchState.Empty)
                } else {
                    _searchState.postValue(SearchState.Success(foundTracks))
                }
            }
        })
    }

    fun loadHistory() {
        _history.value = searchHistoryInteractor.getHistory()
    }

    fun onTrackClicked(track: Track) {
        searchHistoryInteractor.addTrack(track)
        loadHistory()
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        loadHistory()
        _historyVisible.value = false
    }

    fun onSearchFieldFocusChanged(hasFocus: Boolean, isEmpty: Boolean) {
        val hasHistory = searchHistoryInteractor.getHistory().isNotEmpty()
        _historyVisible.value = hasFocus && isEmpty && hasHistory
    }

    sealed class SearchState {
        object Loading : SearchState()
        object Empty : SearchState()
        object Error : SearchState()
        data class Success(val tracks: List<Track>) : SearchState()
    }
}