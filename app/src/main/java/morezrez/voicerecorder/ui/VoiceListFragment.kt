package morezrez.voicerecorder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import morezrez.voicerecorder.databinding.FragmentVoiceListBinding
import morezrez.voicerecorder.dialogs.DialogDeleteShareEdit
import morezrez.voicerecorder.dialogs.DialogRenameCommunicator
import morezrez.voicerecorder.viewModels.VoiceListFragmentViewModel

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
        requestAd()
    }

    private fun registerObserver() {
        voiceListFragmentViewModel.getVoices().observe(viewLifecycleOwner) { voices ->
            voices.let { recyclerAdapter.submitList(it.reversed()) }
        }
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

    private fun requestAd(){

        TapsellPlus.requestRewardedVideoAd(
            requireActivity(),
            "64c79ac324278e2377d6404e",
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)

                    //Ad is ready to show
                    //Put the ad's responseId to your responseId variable
                    val rewardedResponseId = tapsellPlusAdModel.responseId


                    TapsellPlus.showInterstitialAd(
                        requireActivity(), rewardedResponseId,
                        object : AdShowListener() {
                            override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                                super.onOpened(tapsellPlusAdModel)
                            }

                            override fun onClosed(tapsellPlusAdModel: TapsellPlusAdModel) {
                                super.onClosed(tapsellPlusAdModel)
                            }

                            override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                                super.onError(tapsellPlusErrorModel)
                            }
                        })

                }

                override fun error(message: String) {}
            })

    }}