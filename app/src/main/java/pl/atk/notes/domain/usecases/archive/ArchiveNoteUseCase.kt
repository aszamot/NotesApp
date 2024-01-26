package pl.atk.notes.domain.usecases.archive

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import pl.atk.notes.di.IoDispatcher
import pl.atk.notes.domain.exceptions.NoteIsInTrashException
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.domain.repository.NotesRepository
import java.util.UUID
import javax.inject.Inject

class ArchiveNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher

) {
    @Throws(NoteIsInTrashException::class, NoteNotFoundException::class)
    suspend fun invoke(noteId: UUID) = withContext(dispatcher) {
        notesRepository.archiveNote(noteId)
    }
}