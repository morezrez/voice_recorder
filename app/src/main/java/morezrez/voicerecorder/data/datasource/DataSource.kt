package morezrez.voicerecorder.data.datasource

import androidx.lifecycle.LiveData
import morezrez.voicerecorder.models.AudioRecord
interface DataSource {
    fun getVoices() : LiveData<List<AudioRecord>>
    suspend fun insert(audioRecord: AudioRecord)
    suspend fun deleteVoice(id: Int)
    suspend fun updateVoice(fileName: String, id: Int)
}