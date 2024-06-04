package pl.atk.notes.data.repositoryimpl


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import pl.atk.notes.data.local.LocalNotesDataSource
import pl.atk.notes.di.IoDispatcher
import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.repository.NotesRepository
import pl.atk.notes.domain.utils.SearchNotesQuery
import java.util.UUID
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val localNotesDataSource: LocalNotesDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NotesRepository {

    override fun getAllNotesFlow(): Flow<List<Note>> {
        return localNotesDataSource.getNotesFlow().flowOn(ioDispatcher)
    }

    override fun searchNotesFlow(
        searchNotesQuery: SearchNotesQuery?,
    ): Flow<List<Note>> {
        val query = searchNotesQuery?.query
        return localNotesDataSource.searchNotesFlow(query).flowOn(ioDispatcher)
    }

    override fun getNoteFlow(noteId: UUID?): Flow<Note?> {
        return localNotesDataSource.getNoteFlow(noteId)
            .flowOn(ioDispatcher)
    }

    override suspend fun addNote(note: Note) = withContext(ioDispatcher) {
        localNotesDataSource.addNote(note)
    }

    override suspend fun updateNote(note: Note) = withContext(ioDispatcher) {
        localNotesDataSource.updateNote(note)
    }

    override suspend fun updateNoteTitle(noteId: UUID, title: String?) = withContext(ioDispatcher) {
        localNotesDataSource.updateNoteTitle(noteId, title)
    }

    override suspend fun updateNoteContent(noteId: UUID, content: String?) =
        withContext(ioDispatcher) {
            localNotesDataSource.updateNoteContent(noteId, content)
        }

    override suspend fun deleteNote(noteId: UUID) = withContext(ioDispatcher) {
        localNotesDataSource.deleteNote(noteId)
    }

    override suspend fun deleteAllNotes() = withContext(ioDispatcher) {
        localNotesDataSource.deleteAllNotes()
    }
}