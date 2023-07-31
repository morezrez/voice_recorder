package morezrez.voicerecorder.viewModels

import android.annotation.SuppressLint
import android.media.MediaRecorder
import android.util.Log
import androidx.core.content.PackageManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import morezrez.voicerecorder.data.repositories.VoiceRepository
import morezrez.voicerecorder.models.AudioRecord
import morezrez.voicerecorder.utils.SingleLiveEvent
import morezrez.voicerecorder.utils.Timer
import java.io.IOException
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ListeningFragmentViewModel @Inject constructor(private val repository: VoiceRepository) :
    ViewModel(), Timer.OnTimerUpdateListener {

    private val _rivePlayingLiveData = SingleLiveEvent<Boolean?>(false)
    val rivePlayingLiveData: SingleLiveEvent<Boolean?> = _rivePlayingLiveData

    private val _duration = MutableLiveData("00:00.00")
    val duration: LiveData<String> = _duration

    private val _amp = MutableLiveData(0)
    val amp: LiveData<Int> = _amp

    private lateinit var fileName: String
    private lateinit var dirPath: String
    private var recording = false
    private lateinit var timer: Timer
    private var recorder: MediaRecorder? = null
    private var refreshRate: Long = 60

    fun riveAnimationOnClick() {
        Log.d("viewModel", "rive call")
        _rivePlayingLiveData.value = (_rivePlayingLiveData.value ?: true).not()
    }

    @SuppressLint("RestrictedApi")
    fun startRecording(_fileName: String, _dirPath: String) {
        recording = true

        timer = Timer(this)
        timer.start()

        fileName = _fileName
        dirPath = _dirPath

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(dirPath + fileName)
            try {
                prepare()
            } catch (e: IOException) {
                Log.e(PackageManagerCompat.LOG_TAG, "prepare() failed")
            }
            start()
        }
        animatePlayerView()
    }

    fun stopRecording() {
        val duration = timer.format().split(".")[0]
        recording = false
        try {
            recorder?.apply {
                stop()
                release()
            }
        } catch (e: Exception) {
            Log.d("Stop error", e.toString())
        }
        recorder = null
        try {
            timer.stop()
        } catch (e: Exception) {
            Log.d("Stop timer error", e.toString())
        }
        insertVoice(AudioRecord(fileName, dirPath, Date().time, duration))
        _duration.value = "00:00.00"
    }

    private fun animatePlayerView() {
        if (recording) {
            val amp = recorder!!.maxAmplitude
            _amp.value = amp
            viewModelScope.launch {
                delay(refreshRate)
                animatePlayerView()
            }

        }
    }

    override fun onTimerUpdate(duration: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                if (recording)
                    _duration.value = duration
            }
        }
    }

    private fun insertVoice(audioRecord: AudioRecord) {
        viewModelScope.launch {
            repository.insert(audioRecord)
        }
    }
}