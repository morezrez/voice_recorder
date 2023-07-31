package morezrez.voicerecorder.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import morezrez.voicerecorder.data.database.VoiceRoomDataBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Volatile
    private var INSTANCE: VoiceRoomDataBase? = null

    @Provides
    @Singleton
    fun getDataBase(@ApplicationContext appContext: Context): VoiceRoomDataBase {
        // TODO(SHAYAN): Problem, This is not thread safe
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                appContext.applicationContext,
                VoiceRoomDataBase::class.java,
                "voice_database"
            )
                .build()
            INSTANCE = instance
            instance
        }
    }
}