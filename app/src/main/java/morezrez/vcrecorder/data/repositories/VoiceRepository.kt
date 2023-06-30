package morezrez.vcrecorder.data.repositories

import androidx.lifecycle.LiveData
import morezrez.vcrecorder.data.datasource.VoiceLocalDataSource
import javax.inject.Inject

class VoiceRepository @Inject constructor(private val voiceLocalDataSource: VoiceLocalDataSource) {

    fun getVoices(): LiveData<List<morezrez.vcrecorder.models.AudioRecord>> =
        voiceLocalDataSource.getVoices()

    suspend fun insert(audioRecord: morezrez.vcrecorder.models.AudioRecord) =
        voiceLocalDataSource.insert(audioRecord)

    suspend fun deleteVoice(id: Int) = voiceLocalDataSource.deleteVoice(id)

    suspend fun updateVoice(fileName: String, id: Int) =
        voiceLocalDataSource.updateVoice(fileName, id)
}