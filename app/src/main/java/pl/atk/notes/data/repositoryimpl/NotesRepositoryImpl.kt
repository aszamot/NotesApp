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

    override suspend fun archiveNote(note: Note) {
        localNotesDataSource.updateNote(
            note.copy(
                isArchived = true,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun unArchiveNote(note: Note) {
        localNotesDataSource.updateNote(
            note.copy(
                isArchived = false,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun trashNote(note: Note) {
        localNotesDataSource.updateNote(
            note.copy(
                isInTrash = true,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun unTrashNote(note: Note) {
        localNotesDataSource.updateNote(
            note.copy(
                isInTrash = false,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun deleteNote(note: Note) {
        localNotesDataSource.deleteNote(note)
    }

    override fun getNotesFlow(): Flow<List<Note>> {
        return localNotesDataSource.getNotesFlow()
    }
}