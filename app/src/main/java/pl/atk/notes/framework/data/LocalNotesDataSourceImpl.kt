package pl.atk.notes.framework.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pl.atk.notes.data.local.LocalNotesDataSource
import pl.atk.notes.di.IoDispatcher
import pl.atk.notes.domain.exceptions.NoteIsInTrashException
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.domain.models.Note
import pl.atk.notes.framework.db.daos.NotesDao
import pl.atk.notes.framework.db.models.NoteEntity
import pl.atk.notes.utils.extensions.empty
import pl.atk.notes.utils.extensions.toNote
import pl.atk.notes.utils.extensions.toNoteEntity
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class LocalNotesDataSourceImpl @Inject constructor(
    private val notesDao: NotesDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocalNotesDataSource {

    override suspend fun addNote(note: Note) = withContext(ioDispatcher) {
        notesDao.addNote(note.toNoteEntity())
    }

    @Throws(NoteIsInTrashException::class, NoteNotFoundException::class)
    override suspend fun archiveNote(noteId: UUID) = withContext(ioDispatcher) {
        val note = tryToGetNote(noteId)
        if (note.isInTrash)
            throw NoteIsInTrashException()
        else
            notesDao.update(
                note.copy(
                    isArchived = true,
                    timestamp = System.currentTimeMillis()
                )
            )
    }

    @Throws(NoteIsInTrashException::class, NoteNotFoundException::class)
    override suspend fun unArchiveNote(noteId: UUID) = withContext(ioDispatcher) {
        val note = tryToGetNote(noteId)
        if (note.isInTrash)
            throw NoteIsInTrashException()
        else
            notesDao.update(
                note.copy(
                    isArchived = false,
                    timestamp = System.currentTimeMillis()
                )
            )
    }

    @Throws(NoteNotFoundException::class)
    override suspend fun trashNote(noteId: UUID) = withContext(ioDispatcher) {
        val note = tryToGetNote(noteId)
        notesDao.update(
            note.copy(
                isInTrash = true,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    @Throws(NoteNotFoundException::class)
    override suspend fun unTrashNote(noteId: UUID) = withContext(ioDispatcher) {
        val note = tryToGetNote(noteId)
        notesDao.update(
            note.copy(
                isInTrash = false,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun deleteNote(noteId: UUID) = withContext(ioDispatcher) {
        notesDao.delete(noteId)
    }

    override fun searchNotesFlow(query: String?): Flow<List<Note>> {
        return notesDao.searchNotesFlow(query ?: String.empty)
            .flowOn(ioDispatcher)
            .distinctUntilChanged()
            .map { notes ->
                notes.map { it.toNote() }
            }
    }

    override fun getNotesFlow(): Flow<List<Note>> {
        return notesDao.getAllNotesFlow()
            .flowOn(ioDispatcher)
            .distinctUntilChanged()
            .map { notes ->
                notes.map { it.toNote() }
            }
    }

    override fun getArchivedNotesFlow(query: String?): Flow<List<Note>> {
        return notesDao.getArchivedNotesFlow(query ?: String.empty)
            .flowOn(ioDispatcher)
            .distinctUntilChanged()
            .map { notes ->
                notes.map { it.toNote() }
            }
    }

    override fun getInTrashNotesFlow(query: String?): Flow<List<Note>> {
        return notesDao.getInTrashNotesFlow(query ?: String.empty)
            .flowOn(ioDispatcher)
            .distinctUntilChanged()
            .map { notes ->
                notes.map { it.toNote() }
            }
    }

    @Throws(NoteNotFoundException::class)
    private suspend fun tryToGetNote(noteId: UUID): NoteEntity {
        val note = notesDao.getNote(noteId)
        if (note == null) {
            throw NoteNotFoundException()
        } else
            return note
    }
}
