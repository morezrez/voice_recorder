package morezrez.vcrecorder.viewModels

import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import morezrez.vcrecorder.R
import morezrez.vcrecorder.data.repositories.VoiceRepository
import javax.inject.Inject

@HiltViewModel
class PlayingFragmentViewModel @Inject constructor(repository: VoiceRepository) : ViewModel() {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private val delay = 100L
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private var playbackSpeed: Float = 1.0f


    private val _playing = MutableLiveData(false)
    val playing: LiveData<Boolean> = _playing

    private val _audioSessionId = MutableLiveData(-1)
    val audioSessionId: LiveData<Int> = _audioSessionId

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _duration = MutableLiveData(0)
    val duration: LiveData<Int> = _duration

    private val _onComplete = MutableLiveData(false)
    val onComplete: LiveData<Boolean> = _onComplete

    fun initMediaPlayer(filePath: String) {
        mediaPlayer.apply {
            setDataSource(filePath)
            prepare()
            _audioSessionId.value = mediaPlayer.audioSessionId
            _duration.value = mediaPlayer.duration
            handler = Handler(Looper.getMainLooper())
        }
    }


    fun playPausePlayer() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            runnable = Runnable {
                _progress.value = mediaPlayer.currentPosition
                handler.postDelayed(runnable, delay)
            }
            handler.postDelayed(runnable, delay)
            _playing.value = true
        } else {
            mediaPlayer.pause()
            handler.removeCallbacks(runnable)
            _playing.value = false
        }

        mediaPlayer.setOnCompletionListener {
            _onComplete.value = true
        }

    }

    fun stopPlayer() {
        handler.removeCallbacks(runnable)
    }

    fun onBackPressed() {
        mediaPlayer.stop()
        mediaPlayer.release()
        handler.removeCallbacks(runnable)
    }

    fun forward() {
        mediaPlayer.seekTo(mediaPlayer.currentPosition + 1000)
    }

    fun backward() {
        mediaPlayer.seekTo(mediaPlayer.currentPosition - 1000)
    }

    fun seekTo(p1: Int) {
        mediaPlayer.seekTo(p1)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun chipSpeed(): Float {
        when (playbackSpeed) {
            0.5f -> playbackSpeed += 0.5f
            1.0f -> playbackSpeed += 0.5f
            1.5f -> playbackSpeed += 0.5f
            2.0f -> playbackSpeed = 0.5f
        }
        mediaPlayer.playbackParams = PlaybackParams().setSpeed(playbackSpeed)
        return playbackSpeed
    }
}