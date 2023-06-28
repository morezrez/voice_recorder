package morezrez.vcrecorder.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import morezrez.vcrecorder.databinding.EditedRenameDialogBinding
import morezrez.vcrecorder.databinding.RenameDialogBinding

@AndroidEntryPoint
class DialogRename(
    private val _id: Int,
    val name: String,
    private val dir: String,
    private val dialogCommunicator: DialogRenameCommunicator?
) : DialogFragment() {
    private lateinit var binding: EditedRenameDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(morezrez.vcrecorder.R.drawable.dialog_frame2);
        binding = EditedRenameDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOk.setOnClickListener {
            dialogCommunicator?.rename(
                _id,
                name,
                dir,
                binding.edtNewVoiceTitle.text.toString()
            )
            dialog?.dismiss()

        }
        binding.txtCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

}

interface DialogRenameCommunicator {
    fun rename(
        id: Int,
        name: String,
        dir: String,
        newName: String
    )
}