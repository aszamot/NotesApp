package pl.atk.notes.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import pl.atk.notes.di.IoDispatcher
import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.repository.NotesRepository
import pl.atk.notes.utils.extensions.empty
import java.util.UUID
import javax.inject.Inject

class AddEmptyNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() : UUID = withContext(ioDispatcher) {
        val note = Note(
            id = UUID.randomUUID(),
            title = String.empty,
            content = String.empty,
            timestamp = System.currentTimeMillis()
        )
        notesRepository.addNote(note)
        return@withContext note.id
    }
}