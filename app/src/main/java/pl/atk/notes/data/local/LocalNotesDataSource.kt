package pl.atk.notes.data.local

import kotlinx.coroutines.flow.Flow
import pl.atk.notes.domain.models.Note
import java.util.UUID

interface LocalNotesDataSource {
    suspend fun addNote(note: Note)
    suspend fun updateNoteTitle(noteId: UUID, title: String?)
    suspend fun updateNoteContent(noteId: UUID, content: String?)
    suspend fun deleteNote(noteId: UUID)
    fun getNoteFlow(noteId: UUID?): Flow<Note?>
    fun getNotesFlow(): Flow<List<Note>>
    fun searchNotesFlow(query: String?): Flow<List<Note>>
}