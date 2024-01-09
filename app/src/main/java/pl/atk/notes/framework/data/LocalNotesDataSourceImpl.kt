package pl.atk.notes.framework.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.atk.notes.data.local.LocalNotesDataSource
import pl.atk.notes.domain.models.Note
import pl.atk.notes.framework.db.daos.NotesDao
import pl.atk.notes.framework.db.models.NoteEntity

class LocalNotesDataSourceImpl(
    private val notesDao: NotesDao
) : LocalNotesDataSource {

    override suspend fun addNote(note: Note) {
        notesDao.insert(note.toNoteEntity())
    }

    override suspend fun deleteNote(note: Note) {
        notesDao.delete(note.toNoteEntity())
    }

    override fun getNotesFlow(): Flow<List<Note>> {
        return notesDao.getNotesFlow()
            .map { notes ->
                notes.map { it.toNote() }
            }
    }

    private fun Note.toNoteEntity() = NoteEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        timestamp = this.timestamp,
        isInTrash = this.isInTrash,
        isArchived = this.isArchived
    )

    private fun NoteEntity.toNote() = Note(
        id = this.id,
        title = this.title,
        content = this.content,
        timestamp = this.timestamp,
        isInTrash = this.isInTrash,
        isArchived = this.isArchived
    )
}
