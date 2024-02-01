package pl.atk.notes.data.local

import kotlinx.coroutines.flow.Flow
import pl.atk.notes.domain.models.Note
import java.util.UUID

interface LocalNotesDataSource {
    suspend fun addNote(note: Note)
    suspend fun archiveNote(noteId: UUID)
    suspend fun unArchiveNote(noteId: UUID)
    suspend fun trashNote(noteId: UUID)
    suspend fun unTrashNote(noteId: UUID)
    suspend fun deleteNote(noteId: UUID)
    fun getNotesFlow(): Flow<List<Note>>
    fun searchNotesFlow(query: String?): Flow<List<Note>>
    fun getArchivedNotesFlow(): Flow<List<Note>>
    fun searchArchivedNotesFlow(query: String?): Flow<List<Note>>
    fun getInTrashNotesFlow(): Flow<List<Note>>
}