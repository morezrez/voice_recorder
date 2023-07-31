package morezrez.voicerecorder.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import morezrez.voicerecorder.R
import morezrez.voicerecorder.databinding.RatingDialogBinding


class DialogRate : DialogFragment() {

    private lateinit var binding: RatingDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_frame);
        binding = RatingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.noRate.setOnClickListener {
            dialog?.dismiss()
        }

        binding.yesRate.setOnClickListener {
            val intent = Intent(Intent.ACTION_EDIT)
            intent.data = Uri.parse("bazaar://details?id=" + "morezrez.voicerecorder")
            intent.setPackage("com.farsitel.bazaar")
            startActivity(intent)
            dialog?.dismiss()
        }

    }
}