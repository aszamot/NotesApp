package pl.atk.notes.domain.usecases.delete

import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.repository.NotesRepository

class TrashNoteUseCase(
    private val notesRepository: NotesRepository
) {
    suspend fun invoke(note: Note) {
        notesRepository.trashNote(note)
    }
}