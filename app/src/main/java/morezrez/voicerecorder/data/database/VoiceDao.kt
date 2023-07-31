package morezrez.voicerecorder.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import morezrez.voicerecorder.models.AudioRecord
@Dao
interface VoiceDao {
    @Query("SELECT * FROM audioRecords")
    fun getVoices(): LiveData<List<AudioRecord>>

    @Query("DELETE FROM audioRecords WHERE id=:id")
    fun deleteVoice(id : Int)

    @Query("UPDATE audioRecords SET fileName = :fileName WHERE id=:id")
    fun updateVoice(fileName : String , id : Int)

    @Insert
    fun insert(audioRecord: AudioRecord)
}