package pl.atk.notes.data.repositoryimpl


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import pl.atk.notes.data.local.LocalNotesDataSource
import pl.atk.notes.di.IoDispatcher
import pl.atk.notes.domain.exceptions.NoteIsInTrashException
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.repository.NotesRepository
import pl.atk.notes.domain.utils.FilterNotesByType
import pl.atk.notes.domain.utils.SearchNotesQuery
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val localNotesDataSource: LocalNotesDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NotesRepository {

    override fun getAllNotesFlow(filterNotesByType: FilterNotesByType?): Flow<List<Note>> {
        val flow = when (filterNotesByType) {
            is FilterNotesByType.Archived -> localNotesDataSource.getArchivedNotesFlow()
            is FilterNotesByType.InTrash -> localNotesDataSource.getInTrashNotesFlow()
            else -> localNotesDataSource.getNotesFlow()
        }
        return flow.flowOn(ioDispatcher)
    }

    override fun searchNotesFlow(
        searchNotesQuery: SearchNotesQuery?,
        filterNotesByType: FilterNotesByType?
    ): Flow<List<Note>> {
        val query = searchNotesQuery?.query
        val flow = when (filterNotesByType) {
            is FilterNotesByType.Archived -> localNotesDataSource.getArchivedNotesFlow(query)
            is FilterNotesByType.InTrash -> localNotesDataSource.getInTrashNotesFlow(query)
            else -> localNotesDataSource.searchNotesFlow(query)
        }
        return flow.flowOn(ioDispatcher)
    }

    override suspend fun addNote(note: Note) = withContext(ioDispatcher) {
        localNotesDataSource.addNote(note)
    }

    @Throws(NoteIsInTrashException::class, NoteNotFoundException::class)
    override suspend fun archiveNote(noteId: UUID) = withContext(ioDispatcher) {
        localNotesDataSource.archiveNote(noteId)
    }

    @Throws(NoteIsInTrashException::class, NoteNotFoundException::class)
    override suspend fun unArchiveNote(noteId: UUID) = withContext(ioDispatcher) {
        localNotesDataSource.unArchiveNote(noteId)
    }

    @Throws(NoteNotFoundException::class)
    override suspend fun trashNote(noteId: UUID) = withContext(ioDispatcher) {
        localNotesDataSource.trashNote(noteId)
    }

    @Throws(NoteNotFoundException::class)
    override suspend fun unTrashNote(noteId: UUID) = withContext(ioDispatcher) {
        localNotesDataSource.unTrashNote(noteId)
    }

    override suspend fun deleteNote(noteId: UUID) = withContext(ioDispatcher) {
        localNotesDataSource.deleteNote(noteId)
    }
}