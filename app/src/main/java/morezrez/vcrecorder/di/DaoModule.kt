package morezrez.vcrecorder.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import morezrez.vcrecorder.data.database.VoiceDao
import morezrez.vcrecorder.data.database.VoiceRoomDataBase
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