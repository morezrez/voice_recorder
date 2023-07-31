package morezrez.voicerecorder.data.repositories

import androidx.lifecycle.LiveData
import morezrez.voicerecorder.data.datasource.VoiceLocalDataSource
import javax.inject.Inject

class VoiceRepository @Inject constructor(private val voiceLocalDataSource: VoiceLocalDataSource) {

    fun getVoices(): LiveData<List<morezrez.voicerecorder.models.AudioRecord>> =
        voiceLocalDataSource.getVoices()

    suspend fun insert(audioRecord: morezrez.voicerecorder.models.AudioRecord) =
        voiceLocalDataSource.insert(audioRecord)

    suspend fun deleteVoice(id: Int) = voiceLocalDataSource.deleteVoice(id)

    suspend fun updateVoice(fileName: String, id: Int) =
        voiceLocalDataSource.updateVoice(fileName, id)
}