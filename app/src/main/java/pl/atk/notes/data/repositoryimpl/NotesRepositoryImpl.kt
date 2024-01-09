package pl.atk.notes.data.repositoryimpl

import kotlinx.coroutines.flow.Flow
import pl.atk.notes.data.local.LocalNotesDataSource
import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.repository.NotesRepository

class NotesRepositoryImpl(
    private val localNotesDataSource: LocalNotesDataSource
) : NotesRepository {

    override suspend fun addNote(note: Note) {
        localNotesDataSource.addNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        localNotesDataSource.deleteNote(note)
    }

    override fun getNotes(): Flow<List<Note>> {
        return localNotesDataSource.getNotesFlow()
    }
}