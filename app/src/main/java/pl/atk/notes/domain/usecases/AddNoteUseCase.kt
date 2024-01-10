package pl.atk.notes.domain.usecases

import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.repository.NotesRepository

class AddNoteUseCase(
    private val notesRepository: NotesRepository
) {

    suspend fun invoke(note: Note) = notesRepository.addNote(note)
}