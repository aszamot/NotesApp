package pl.atk.notes.framework.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pl.atk.notes.data.local.LocalNotesDataSource
import pl.atk.notes.di.IoDispatcher
import pl.atk.notes.domain.models.Note
import pl.atk.notes.framework.db.daos.NotesDao
import pl.atk.notes.utils.extensions.empty
import pl.atk.notes.utils.extensions.toNote
import pl.atk.notes.utils.extensions.toNoteEntity
import java.util.UUID
import javax.inject.Inject

class LocalNotesDataSourceImpl @Inject constructor(
    private val notesDao: NotesDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocalNotesDataSource {

    override suspend fun addNote(note: Note) = withContext(ioDispatcher) {
        notesDao.addNote(note.toNoteEntity())
    }

    override suspend fun updateNote(note: Note) = withContext(ioDispatcher) {
        notesDao.update(note.toNoteEntity())
    }

    override suspend fun updateNoteTitle(noteId: UUID, title: String?): Unit =
        withContext(ioDispatcher) {
            notesDao.getNote(noteId)?.let { note ->
                val newNote = note.copy(
                    title = title,
                    timestamp = System.currentTimeMillis()
                )
                notesDao.update(newNote)
            }
        }

    override suspend fun updateNoteContent(noteId: UUID, content: String?): Unit =
        withContext(ioDispatcher) {
            notesDao.getNote(noteId)?.let { note ->
                val newNote = note.copy(
                    content = content,
                    timestamp = System.currentTimeMillis()
                )
                notesDao.update(newNote)
            }
        }

    override suspend fun deleteNote(noteId: UUID) = withContext(ioDispatcher) {
        notesDao.delete(noteId)
    }

    override suspend fun deleteAllNotes() = withContext(ioDispatcher) {
        notesDao.deleteAllNotes()
    }

    override fun getNoteFlow(noteId: UUID?): Flow<Note?> {
        return notesDao.getNoteFlow(noteId)
            .flowOn(ioDispatcher)
            .map { note ->
                note?.toNote()
            }
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
}
