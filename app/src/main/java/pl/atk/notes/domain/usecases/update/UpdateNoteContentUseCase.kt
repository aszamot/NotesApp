package pl.atk.notes.domain.usecases.update

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import pl.atk.notes.di.IoDispatcher
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.domain.repository.NotesRepository
import java.util.UUID
import javax.inject.Inject

class UpdateNoteContentUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    @Throws(NoteNotFoundException::class)
    suspend operator fun invoke(noteId: UUID, content: String?) = withContext(ioDispatcher) {
        notesRepository.updateNoteContent(noteId, content)
    }
}