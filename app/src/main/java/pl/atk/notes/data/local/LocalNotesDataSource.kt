package pl.atk.notes.data.local

import kotlinx.coroutines.flow.Flow
import pl.atk.notes.domain.models.Note

interface LocalNotesDataSource {

    suspend fun addNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getNotesFlow(): Flow<List<Note>>
}