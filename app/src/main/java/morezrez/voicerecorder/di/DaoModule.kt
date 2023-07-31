package morezrez.voicerecorder.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import morezrez.voicerecorder.data.database.VoiceDao
import morezrez.voicerecorder.data.database.VoiceRoomDataBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Singleton
    fun provideDao(database: VoiceRoomDataBase): VoiceDao {
        return database.voiceDao()
    }
}