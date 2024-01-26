package pl.atk.notes.domain.usecases.delete

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import pl.atk.notes.di.IoDispatcher
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.repository.NotesRepository
import java.util.UUID
import javax.inject.Inject

class TrashNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    @Throws(NoteNotFoundException::class)
    suspend fun invoke(noteId: UUID) = withContext(ioDispatcher) {
        notesRepository.trashNote(noteId)
    }
}