package morezrez.vcrecorder.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import morezrez.vcrecorder.databinding.FragmentListeningBinding
import morezrez.vcrecorder.viewModels.ListeningFragmentViewModel
import java.text.SimpleDateFormat
import java.util.Date

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

@AndroidEntryPoint
class ListeningFragment : Fragment() {

    private lateinit var fileName: String
    private lateinit var dirPath: String
    private lateinit var handler: Handler
    private var openDrawer : Boolean = false

    // Requesting permission to RECORD_AUDIO
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    private val listeningFragmentViewModel: ListeningFragmentViewModel by viewModels()
    private lateinit var binding: FragmentListeningBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListeningBinding.inflate(inflater, container, false)
        // Record to the external cache directory for visibility
        ActivityCompat.requestPermissions(
            requireActivity(),
            permissions,
            REQUEST_RECORD_AUDIO_PERMISSION
        )

        handler = Handler(Looper.myLooper()!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerObserver()

        binding.riveAnimationViewClick.setOnClickListener {
            listeningFragmentViewModel.riveAnimationOnClick()
        }
        listeningFragmentViewModel.rivePlayingLiveData.observe(viewLifecycleOwner) { isPlaying ->
            isPlaying?: return@observe
            if (isPlaying == true) {
                lifecycleScope.launch {
                    stopRecording()
                    delay(60)
                    stopAnimating()
                    Log.d("StopCall", "recorder stop called!")
                }
            } else {
                startRecording()
                startAnimating()
            }
        }
    }

    private fun registerObserver() {

        listeningFragmentViewModel.duration.observe(viewLifecycleOwner) { duration ->
            binding.timerView.text = duration
        }

        listeningFragmentViewModel.amp.observe(viewLifecycleOwner) { amp ->
            binding.recorderWaveformView.updateAmps(amp)
        }

    }

    private fun stopAnimating() {
        binding.riveAnimationView.reset()
        binding.riveAnimationView.stop()
    }

    private fun startAnimating() {
        binding.riveAnimationView.play()
    }

    @SuppressLint("RestrictedApi", "SimpleDateFormat")
    private fun startRecording() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissions,
                REQUEST_RECORD_AUDIO_PERMISSION
            )
            return
        }

        val pattern = "yyyy.MM.dd_hh.mm.ss"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(Date())

        dirPath = "${requireActivity().externalCacheDir?.absolutePath}/"
        fileName = "voice_record_${date}.mp3"

        listeningFragmentViewModel.startRecording(fileName, dirPath)
    }

    private fun stopRecording() {
        binding.recorderWaveformView.reset()
        listeningFragmentViewModel.stopRecording()
    }

    companion object {
    }

}


