package pl.atk.notes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.atk.notes.data.local.LocalNotesDataSource
import pl.atk.notes.data.repositoryimpl.NotesRepositoryImpl
import pl.atk.notes.domain.repository.NotesRepository
import pl.atk.notes.framework.data.LocalNotesDataSourceImpl
import pl.atk.notes.framework.db.daos.NotesDao

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideLocalDataSource(notesDao: NotesDao): LocalNotesDataSource =
        LocalNotesDataSourceImpl(notesDao)

    @Provides
    fun provideNotesRepository(localNotesDataSource: LocalNotesDataSource): NotesRepository =
        NotesRepositoryImpl(localNotesDataSource)
}