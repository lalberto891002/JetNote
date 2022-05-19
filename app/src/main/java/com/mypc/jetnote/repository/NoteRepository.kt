package com.mypc.jetnote.repository

import android.view.KeyEvent
import com.mypc.jetnote.data.NoteDatabaseDao
import com.mypc.jetnote.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDatabaseDao: NoteDatabaseDao) {

    suspend fun addNote(note: Note) = noteDatabaseDao.insert(note)
    suspend fun updateNote(note:Note) = noteDatabaseDao.update(note)
    suspend fun deleteNote(note:Note) = noteDatabaseDao.deleteNote(note)
    suspend fun deleteAllNotes() = noteDatabaseDao.deleteAll()


    suspend fun getNoteById(id: String):Note = noteDatabaseDao.getNoteById(id)

    suspend fun getAllNotes(): Flow<List<Note>> = noteDatabaseDao.getNotes().flowOn(Dispatchers.IO)
        .conflate()

}