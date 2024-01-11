package pl.atk.notes.framework.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.atk.notes.data.local.LocalNotesDataSource
import pl.atk.notes.domain.models.Note
import pl.atk.notes.framework.db.daos.NotesDao
import pl.atk.notes.utils.extensions.toNote
import pl.atk.notes.utils.extensions.toNoteEntity

class LocalNotesDataSourceImpl(
    private val notesDao: NotesDao
) : LocalNotesDataSource {

    override suspend fun addNote(note: Note) {
        notesDao.addNote(note.toNoteEntity())
    }

    override suspend fun updateNote(note: Note) {
        notesDao.update(note.toNoteEntity())
    }

    override suspend fun deleteNote(note: Note) {
        notesDao.delete(note.toNoteEntity())
    }

    override fun getNotesFlow(query: String?): Flow<List<Note>> {
        return notesDao.getNotesFlow(query)
            .map { notes ->
                notes.map { it.toNote() }
            }
    }
}
