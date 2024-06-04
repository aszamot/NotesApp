package pl.atk.notes.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import pl.atk.notes.di.IoDispatcher
import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.repository.NotesRepository
import java.util.UUID
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(noteId: UUID?): Flow<Note?> =
        notesRepository.getNoteFlow(noteId).flowOn(ioDispatcher)
}