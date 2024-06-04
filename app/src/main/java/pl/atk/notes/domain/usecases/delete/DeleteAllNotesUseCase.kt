package pl.atk.notes.domain.usecases.delete

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import pl.atk.notes.di.IoDispatcher
import pl.atk.notes.domain.repository.NotesRepository
import javax.inject.Inject

class DeleteAllNotesUseCase @Inject constructor(
    private val notesRepositoryImpl: NotesRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke() = withContext(ioDispatcher) {
        notesRepositoryImpl.deleteAllNotes()
    }
}