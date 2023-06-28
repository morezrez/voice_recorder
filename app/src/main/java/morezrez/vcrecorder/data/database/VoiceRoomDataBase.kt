package morezrez.vcrecorder.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import morezrez.vcrecorder.models.AudioRecord

@Database(entities = [AudioRecord::class], version = 1, exportSchema = false)
abstract class VoiceRoomDataBase : RoomDatabase() {
    abstract fun voiceDao(): VoiceDao
}