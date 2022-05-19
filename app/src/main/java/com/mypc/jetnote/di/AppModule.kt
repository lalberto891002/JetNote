package com.mypc.jetnote.di

import android.content.Context
import androidx.room.Room
import com.mypc.jetnote.data.NoteDataBase
import com.mypc.jetnote.data.NoteDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideNotesDao(noteDatabase: NoteDataBase): NoteDatabaseDao = noteDatabase.noteDao()

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context):NoteDataBase {
        val build = Room.databaseBuilder(
            context,
            NoteDataBase::class.java,
            "notes_db"
        ).fallbackToDestructiveMigration().build()
        return build
    }

}