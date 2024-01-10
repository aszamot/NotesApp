package pl.atk.notes.domain.usecases.archive

import pl.atk.notes.domain.exceptions.NoteIsInTrashException
import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.repository.NotesRepository

class UnarchiveNoteUseCase(
    private val notesRepository: NotesRepository
) {

    @Throws(NoteIsInTrashException::class)
    suspend fun invoke(note: Note) {
        if (note.isInTrash) throw NoteIsInTrashException()
        notesRepository.unArchiveNote(note)
    }
}