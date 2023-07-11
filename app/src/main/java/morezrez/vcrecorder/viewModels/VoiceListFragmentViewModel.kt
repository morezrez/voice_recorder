package morezrez.vcrecorder.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import morezrez.vcrecorder.data.repositories.VoiceRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VoiceListFragmentViewModel @Inject constructor(private val repository: VoiceRepository) : ViewModel() {

    fun getVoices() = repository.getVoices()

    fun deleteVoice(id : Int , name : String , dir : String) {
        viewModelScope.launch {
            repository.deleteVoice(id)}
        val file = File(dir, name)
        file.delete()
    }

    fun updateVoice(id : Int , newName : String , oldName : String , dir : String){
        viewModelScope.launch{
            repository.updateVoice("$newName.mp3" , id)
            val file = File(dir, oldName)
            val file2 = File(dir, "$newName.mp3")
            file.renameTo(file2)
        }
    }
}