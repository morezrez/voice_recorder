package morezrez.voicerecorder.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import morezrez.voicerecorder.R
import morezrez.voicerecorder.databinding.FragmentPlayingBinding
import morezrez.voicerecorder.utils.DrawerLocker
import morezrez.voicerecorder.viewModels.PlayingFragmentViewModel

@AndroidEntryPoint
class PlayingFragment : Fragment() {

    private val playingFragmentViewModel: PlayingFragmentViewModel by viewModels()
    private lateinit var binding: FragmentPlayingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlayingBinding.inflate(layoutInflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        (activity as DrawerLocker?)!!.setDrawerEnabled(false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val args = bundle?.let { PlayingFragmentArgs.fromBundle(it) }
        val filePath = args?.filePath.toString()
        val filename = args?.fileName

        playingFragmentViewModel.initMediaPlayer(filePath)

        playingFragmentViewModel.audioSessionId.observe(viewLifecycleOwner) { audioSessionId ->
            if (audioSessionId != -1) {
                binding.mVisualizer.setAudioSessionId(audioSessionId)
            }
        }

        playingFragmentViewModel.duration.observe(viewLifecycleOwner) { duration ->
            binding.seekBar.max = duration
        }

        playPausePlayer()

        playingFragmentViewModel.onComplete.observe(viewLifecycleOwner) { onComplete ->
            if (onComplete) stopPlayer()
        }

        binding.btnPlay.setOnClickListener {
            playPausePlayer()
        }

        binding.btnForward.setOnClickListener {
            playingFragmentViewModel.forward()
            binding.seekBar.progress += 1000
        }

        binding.btnBackward.setOnClickListener {
            playingFragmentViewModel.backward()
            binding.seekBar.progress -= 1000
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) playingFragmentViewModel.seekTo(p1)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.chip.setOnClickListener {
            val playbackSpeed = playingFragmentViewModel.chipSpeed()
            binding.chip.text = "x $playbackSpeed "
        }
    }

    private fun playPausePlayer() {
        playingFragmentViewModel.playPausePlayer()

        playingFragmentViewModel.playing.observe(viewLifecycleOwner) { playing ->
            if (playing) {
                binding.btnPlay.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_pause_circle, null)
            } else {
                binding.btnPlay.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_play_circle, null)
            }
        }
        playingFragmentViewModel.progress.observe(viewLifecycleOwner) { progress ->
            binding.seekBar.progress = progress
        }
    }

    private fun stopPlayer() {
        binding.btnPlay.background =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_play_circle, null)
        playingFragmentViewModel.stopPlayer()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                playingFragmentViewModel.onBackPressed()
                super.remove()
                requireActivity().onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            callback
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding.mVisualizer != null) {
            binding.mVisualizer.release()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).supportActionBar?.show()
    }
}