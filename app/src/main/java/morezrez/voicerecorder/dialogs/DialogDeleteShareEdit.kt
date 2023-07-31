package morezrez.voicerecorder.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import morezrez.voicerecorder.R
import morezrez.voicerecorder.databinding.DeleteShareEditDialogBinding
import java.io.File

@AndroidEntryPoint
class DialogDeleteShareEdit(
    private val id: Int,
    private val name: String,
    private val dir: String,
    private val deleteVoice: (id: Int, name: String, dir: String) -> Unit,
    private val dialogRenameCommunicator: DialogRenameCommunicator
) : DialogFragment() {

    private lateinit var binding: DeleteShareEditDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_frame);
        binding = DeleteShareEditDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.relativeDelete.setOnClickListener {
            deleteVoice(id, name, dir)
            dialog?.dismiss()
        }

        binding.relativeRename.setOnClickListener {
            DialogRename(id, name, dir, dialogRenameCommunicator).show(requireActivity().supportFragmentManager, "my tag")
            dialog?.dismiss()

        }

        binding.relativeShare.setOnClickListener {
            val file = File(dir, name)
            val outputFileUri: Uri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                file
            )
            val intent = Intent(Intent.ACTION_SEND)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "audio/*"
            intent.putExtra(Intent.EXTRA_STREAM, outputFileUri)
            requireActivity().startActivity(Intent.createChooser(intent, "Share this audio via"))
            dialog!!.dismiss()
        }
    }
}