package pl.atk.notes.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.utils.FilterNotesByType
import pl.atk.notes.domain.utils.SearchNotesQuery
import java.util.UUID

interface NotesRepository {
    fun getAllNotesFlow(
        filterNotesByType: FilterNotesByType?
    ): Flow<List<Note>>
    fun searchNotesFlow(
        searchNotesQuery: SearchNotesQuery?,
        filterNotesByType: FilterNotesByType?
    ): Flow<List<Note>>
    suspend fun addNote(note: Note)
    suspend fun archiveNote(noteId: UUID)
    suspend fun unArchiveNote(noteId: UUID)
    suspend fun trashNote(noteId: UUID)
    suspend fun unTrashNote(noteId: UUID)
    suspend fun deleteNote(noteId: UUID)
    suspend fun deleteAllNotesInTrash()
}