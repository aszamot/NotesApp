package pl.atk.notes.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.atk.notes.data.local.LocalNotesDataSource
import pl.atk.notes.data.repositoryimpl.NotesRepositoryImpl
import pl.atk.notes.domain.repository.NotesRepository
import pl.atk.notes.framework.data.LocalNotesDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindLocalNotesDataSource(
        localNotesDataSourceImpl: LocalNotesDataSourceImpl
    ): LocalNotesDataSource

    @Binds
    abstract fun bindNotesRepository(
        notesRepositoryImpl: NotesRepositoryImpl
    ): NotesRepository

}