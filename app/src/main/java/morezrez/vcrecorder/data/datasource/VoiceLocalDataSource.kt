package morezrez.vcrecorder.data.datasource

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import morezrez.vcrecorder.data.database.VoiceDao
import morezrez.vcrecorder.models.AudioRecord
import javax.inject.Inject

class VoiceLocalDataSource @Inject constructor(private val voiceDao: VoiceDao) : DataSource {

    override fun getVoices(): LiveData<List<AudioRecord>> = voiceDao.getVoices()

    override suspend fun insert(audioRecord: AudioRecord) {
        withContext(Dispatchers.IO) {
            voiceDao.insert(audioRecord)
        }
    }

    override suspend fun deleteVoice(id: Int) {
        withContext(Dispatchers.IO) {
            voiceDao.deleteVoice(id)
        }
    }

    override suspend fun updateVoice(fileName: String, id: Int) {
        withContext(Dispatchers.IO) {
            voiceDao.updateVoice(fileName, id)
        }
    }
}