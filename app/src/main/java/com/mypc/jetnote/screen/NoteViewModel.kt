package com.mypc.jetnote.screen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mypc.jetnote.data.NotesDataSource
import com.mypc.jetnote.model.Note
import com.mypc.jetnote.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository:NoteRepository): ViewModel() {

    private val _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList = _noteList.asStateFlow()

    //var noteList = mutableStateListOf<Note>()


    init {

        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllNotes().distinctUntilChanged().collect { listOfNotes ->
                if(listOfNotes.isNullOrEmpty()) {
                    Log.d("empty",":Empty List")
                }else {
                    _noteList.value = listOfNotes
                }
            }
        }
        //noteList.addAll(NotesDataSource().loadNotes())
    }

    fun addNote(note:Note) = viewModelScope.launch {
        repository.addNote(note)
    }

    fun updateNote(note:Note) = viewModelScope.launch {
        repository.updateNote(note)
    }

    fun removeNote(note:Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }

    fun getNoteById(id:String):Note {
        var note:Note = Note(title = "", description = "")
        viewModelScope.launch {
             note  = repository.getNoteById(id)

        }

        return note
    }

}



