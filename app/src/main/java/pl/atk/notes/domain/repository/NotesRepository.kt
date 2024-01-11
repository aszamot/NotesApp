package pl.atk.notes.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.atk.notes.domain.models.Note

interface NotesRepository {

    suspend fun addNote(note: Note)
    suspend fun archiveNote(note: Note)
    suspend fun unArchiveNote(note: Note)
    suspend fun trashNote(note: Note)
    suspend fun unTrashNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getNotesFlow(query: String? = null): Flow<List<Note>>
}