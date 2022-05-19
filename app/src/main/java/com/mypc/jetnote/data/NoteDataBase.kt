package com.mypc.jetnote.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mypc.jetnote.model.Note
import com.mypc.jetnote.util.DateConverter
import com.mypc.jetnote.util.UUIDConverter


@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class,UUIDConverter::class)
abstract class NoteDataBase: RoomDatabase() {
    abstract fun noteDao(): NoteDatabaseDao
}