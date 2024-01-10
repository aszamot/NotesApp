package pl.atk.notes.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.atk.notes.domain.models.Note

interface NotesRepository {

    suspend fun addNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getNotesFlow(): Flow<List<Note>>
}