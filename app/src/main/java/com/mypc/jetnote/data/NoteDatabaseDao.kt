package com.mypc.jetnote.data

import androidx.compose.runtime.MutableState
import androidx.room.*
import com.mypc.jetnote.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDatabaseDao {

    @Query(value = "SELECT * FROM NOTES_TBL")
    fun getNotes(): Flow<List<Note>>

    @Query(value = "SELECT * FROM NOTES_TBL WHERE id =:id")
    suspend fun getNoteById(id:String):Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note:Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(note:Note)

    @Query(value = "DELETE FROM notes_tbl")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteNote(note:Note)

}
