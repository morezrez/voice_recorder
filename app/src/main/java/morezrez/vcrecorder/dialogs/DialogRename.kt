package morezrez.vcrecorder.dialogs

import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import morezrez.vcrecorder.databinding.RenameDialogBinding

@AndroidEntryPoint
class DialogRename(
    private val _id: Int,
    val name: String,
    private val dir: String,
    private val dialogCommunicator: DialogRenameCommunicator?
) : DialogFragment() {
    private lateinit var binding: RenameDialogBinding

    override fun onResume() {
        super.onResume()
        val window = dialog!!.window
        val size = Point()
        val display = window!!.windowManager.defaultDisplay
        display.getSize(size)
        val width = size.x
        window.setLayout((width * 0.90).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RenameDialogBinding.inflate(inflater, container, false)
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
        dialog!!.window?.setBackgroundDrawableResource(morezrez.vcrecorder.R.drawable.dialog_frame2)
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