package com.practicum.playlistmaker.presentation.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track
) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Default)
    val playerState: LiveData<PlayerState> = _playerState

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())

    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let { mp ->
                if (mp.isPlaying) {
                    _playerState.value = PlayerState.Playing(
                        dateFormat.format(mp.currentPosition)
                    )
                    handler.postDelayed(this, UPDATE_TIME_DELAY)
                }
            }
        }
    }

    init {
        preparePlayer()
    }

    private fun preparePlayer() {
        val url = track.previewUrl
        if (url.isNullOrEmpty()) {
            _playerState.value = PlayerState.Error
            return
        }

        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            setOnPreparedListener {
                _playerState.value = PlayerState.Prepared()
            }
            setOnCompletionListener {
                _playerState.value = PlayerState.Prepared()
                handler.removeCallbacks(updateTimeRunnable)
            }
            prepareAsync()
        }
    }

    fun playbackControl() {
        when (_playerState.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            else -> {}
        }
    }

    private fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing(
            dateFormat.format(mediaPlayer?.currentPosition ?: 0)
        )
        handler.post(updateTimeRunnable)
    }


    fun pausePlayer() {
        if (_playerState.value is PlayerState.Playing) {
            mediaPlayer?.pause()
            _playerState.value = PlayerState.Paused(
                dateFormat.format(mediaPlayer?.currentPosition ?: 0)
            )
            handler.removeCallbacks(updateTimeRunnable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(updateTimeRunnable)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        private const val UPDATE_TIME_DELAY = 300L
    }

    sealed class PlayerState {
        data class Prepared(val currentTime: String = "00:00") : PlayerState()
        data class Playing(val currentTime: String) : PlayerState()
        data class Paused(val currentTime: String) : PlayerState()
        object Error : PlayerState()
        object Default : PlayerState()
    }
}