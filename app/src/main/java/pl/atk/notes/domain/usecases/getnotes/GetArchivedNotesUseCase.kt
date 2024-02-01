package pl.atk.notes.domain.usecases.getnotes

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import pl.atk.notes.di.IoDispatcher
import pl.atk.notes.domain.repository.NotesRepository
import pl.atk.notes.domain.utils.FilterNotesByType
import javax.inject.Inject

class GetArchivedNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke() = notesRepository.getAllNotesFlow(
        filterNotesByType = FilterNotesByType.Archived
    ).flowOn(ioDispatcher)
}