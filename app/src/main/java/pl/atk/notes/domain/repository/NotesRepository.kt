package pl.atk.notes.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.utils.SearchNotesQuery
import java.util.UUID

interface NotesRepository {
    fun getAllNotesFlow(): Flow<List<Note>>
    fun searchNotesFlow(
        searchNotesQuery: SearchNotesQuery?
    ): Flow<List<Note>>

    fun getNoteFlow(noteId: UUID?): Flow<Note?>
    suspend fun addNote(note: Note)
    suspend fun updateNoteTitle(noteId: UUID, title: String?)
    suspend fun updateNoteContent(noteId: UUID, content: String?)
    suspend fun deleteNote(noteId: UUID)
}