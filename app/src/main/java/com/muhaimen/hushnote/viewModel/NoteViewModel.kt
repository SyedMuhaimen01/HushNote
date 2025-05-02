package com.muhaimen.hushnote.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.muhaimen.hushnote.data.dataclass.Note
import com.muhaimen.hushnote.data.repository.NotesRepository

class NoteViewModel (application: Application) : AndroidViewModel(application){

    private val repository = NotesRepository()

    val notesLiveData = MutableLiveData<List<Note>?>()
    // LiveData for handling success or failure of operations
    val operationStatus = MutableLiveData<Pair<Boolean, String?>>()

    fun addNote(note: Note) {
        repository.addNote(note) { success, message ->
            operationStatus.value = Pair(success, message)
        }
    }

    fun getNotes() {
        repository.getNotes { notes, message ->
            if (notes != null) {
                notesLiveData.value = notes
            } else {
                operationStatus.value = Pair(false, message)
            }
        }
    }

    fun updateNote(noteId: String, updatedNote: Note) {
        repository.updateNote(noteId, updatedNote) { success, message ->
            operationStatus.value = Pair(success, message)
        }
    }

    fun deleteNote(noteId: String) {
        repository.deleteNote(noteId) { success, message ->
            operationStatus.value = Pair(success, message)
        }
    }
}