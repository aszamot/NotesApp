package pl.atk.notes.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import pl.atk.notes.di.IoDispatcher
import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.repository.NotesRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(note: Note) = withContext(ioDispatcher) {
        notesRepository.updateNote(note)
    }
}