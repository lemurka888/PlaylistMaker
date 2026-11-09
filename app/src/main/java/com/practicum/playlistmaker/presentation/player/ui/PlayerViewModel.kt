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

    private val _currentTime = MutableLiveData("00:00")
    val currentTime: LiveData<String> = _currentTime

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())

    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let { mp ->
                if (mp.isPlaying) {
                    _currentTime.value = dateFormat.format(mp.currentPosition)
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
                _playerState.value = PlayerState.Prepared
            }
            setOnCompletionListener {
                _playerState.value = PlayerState.Prepared
                _currentTime.value = "00:00"
                handler.removeCallbacks(updateTimeRunnable)
            }
            prepareAsync()
        }
    }

    fun playbackControl() {
        when (_playerState.value) {
            PlayerState.Playing -> pausePlayer()
            PlayerState.Prepared, PlayerState.Paused -> startPlayer()
            else -> {}
        }
    }

    private fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing
        handler.post(updateTimeRunnable)
    }


    fun pausePlayer() {
        if (_playerState.value == PlayerState.Playing) {
            mediaPlayer?.pause()
            _playerState.value = PlayerState.Paused
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
        object Default : PlayerState()
        object Prepared : PlayerState()
        object Playing : PlayerState()
        object Paused : PlayerState()
        object Error : PlayerState()
    }
}