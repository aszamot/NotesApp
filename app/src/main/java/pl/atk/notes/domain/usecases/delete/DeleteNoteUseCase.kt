package pl.atk.notes.domain.usecases.delete

import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.repository.NotesRepository

class DeleteNoteUseCase(
    private val repository: NotesRepository
) {
    suspend fun invoke(note: Note) = repository.deleteNote(note)
}