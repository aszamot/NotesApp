package pl.atk.notes.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.atk.notes.Globals
import pl.atk.notes.framework.db.NotesDatabase
import pl.atk.notes.framework.db.daos.NotesDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FrameworkModule {

    @Singleton
    @Provides
    fun provideNotesDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            NotesDatabase::class.java,
            Globals.DATABASE_NAME
        ).build()

    @Provides
    fun provideNotesDao(notesDatabase: NotesDatabase): NotesDao =
        notesDatabase.notesDao()
}