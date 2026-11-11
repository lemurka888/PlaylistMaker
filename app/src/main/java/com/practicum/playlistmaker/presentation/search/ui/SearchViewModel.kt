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


    init {
        updateEmptyState()
    }

    fun search(query: String) {
        _searchState.value = SearchState.Loading

        searchTracksInteractor.searchTracks(query, object : SearchTracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                if (errorMessage != null) {
                    _searchState.postValue(SearchState.Error)
                } else if (foundTracks.isNullOrEmpty()) {
                    _searchState.postValue(
                        SearchState.Empty(
                            history = getHistory(),
                            historyVisible = false
                        )
                    )
                } else {
                    _searchState.postValue(SearchState.Success(foundTracks))
                }
            }
        })
    }


    fun onTrackClicked(track: Track) {
        searchHistoryInteractor.addTrack(track)
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        updateEmptyState()
    }

    fun onSearchFieldFocusChanged(hasFocus: Boolean, isEmpty: Boolean) {
        if (isEmpty) {
            updateEmptyState(hasFocus)
        }
    }

    private fun updateEmptyState(hasFocus: Boolean = true) {
        val history = getHistory()
            _searchState.value = SearchState.Empty(
                history = history,
                historyVisible = hasFocus && history.isNotEmpty()
        )
    }

    private fun getHistory(): List<Track> = searchHistoryInteractor.getHistory()

    sealed class SearchState {
        data class Empty(
            val history: List<Track>,
            val historyVisible: Boolean
        ) : SearchState()
        object Loading : SearchState()
        data class Success(val tracks: List<Track>) : SearchState()
        object Error : SearchState()
    }
}