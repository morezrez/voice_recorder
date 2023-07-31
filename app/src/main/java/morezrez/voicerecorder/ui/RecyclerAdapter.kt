package morezrez.voicerecorder.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import morezrez.voicerecorder.R
import morezrez.voicerecorder.models.AudioRecord

class RecyclerAdapter(private val adapterCommunicatorInterface: AdapterCommunicatorInterface) :
    ListAdapter<AudioRecord, RecyclerAdapter.ViewHolder>(
        AUDIO_COMPARATOR
    ) {

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val txtFileTitle: TextView = ItemView.findViewById(R.id.txt_voice)
        val voiceCard: CardView = ItemView.findViewById(R.id.voice_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var recordedAudio = currentList[position]
        holder.txtFileTitle.text = recordedAudio.filename
        val filePath = recordedAudio.filePath+"/"+recordedAudio.filename

        holder.voiceCard.setOnClickListener {
            adapterCommunicatorInterface.setOnVoiceItemClicked(filePath,recordedAudio.filename)
        }

        holder.voiceCard.setOnLongClickListener {
            adapterCommunicatorInterface.setOnLongClicked(recordedAudio.id, recordedAudio.filename, recordedAudio.filePath)
            return@setOnLongClickListener false
        }
    }

    companion object {
        private val AUDIO_COMPARATOR =
            object : DiffUtil.ItemCallback<AudioRecord>() {
                override fun areItemsTheSame(
                    oldItem: AudioRecord,
                    newItem: AudioRecord
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: AudioRecord,
                    newItem: AudioRecord
                ): Boolean {
                    return oldItem.filename == newItem.filename
                }
            }
    }
}

interface AdapterCommunicatorInterface {
    fun setOnLongClicked(id: Int, name: String, dir : String)
    fun setOnVoiceItemClicked(filePath : String , fileName : String)
}