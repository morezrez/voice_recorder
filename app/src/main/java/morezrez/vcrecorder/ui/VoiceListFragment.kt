package morezrez.vcrecorder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import morezrez.vcrecorder.databinding.FragmentVoiceListBinding
import morezrez.vcrecorder.dialogs.DialogDeleteShareEdit
import morezrez.vcrecorder.dialogs.DialogRenameCommunicator
import morezrez.vcrecorder.viewModels.VoiceListFragmentViewModel

@AndroidEntryPoint
class VoiceListFragment : Fragment(),
    AdapterCommunicatorInterface,
    DialogRenameCommunicator{

    private val voiceListFragmentViewModel: VoiceListFragmentViewModel by viewModels()
    private lateinit var binding: FragmentVoiceListBinding
    private lateinit var recyclerAdapter: RecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVoiceListBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerAdapter = RecyclerAdapter(this)
        binding.voicesRecyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        registerObserver()

    }

    private fun registerObserver() {
        voiceListFragmentViewModel.getVoices().observe(viewLifecycleOwner) { voices ->
            voices.let { recyclerAdapter.submitList(it.reversed()) }
        }
    }

    companion object {

    }

    override fun setOnLongClicked(id: Int, name: String, dir: String) {
        DialogDeleteShareEdit(id, name, dir, { id, name, dir ->
            voiceListFragmentViewModel.deleteVoice(id, name, dir)
        }, this).show(requireActivity().supportFragmentManager, "my tag")
    }

    override fun setOnVoiceItemClicked(filePath: String, fileName: String) {
        val action =
            VoiceListFragmentDirections.actionVoiceListFragmentToPlayingFragment(filePath, fileName)
        findNavController().navigate(action)
    }

    override fun rename(id: Int, name: String, dir: String, newName: String) {
        voiceListFragmentViewModel.updateVoice(id, newName, name, dir)
    }



}


