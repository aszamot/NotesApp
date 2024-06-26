package pl.atk.notes.domain.usecases.getnotes

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import pl.atk.notes.di.IoDispatcher
import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.repository.NotesRepository
import pl.atk.notes.domain.utils.SearchNotesQuery
import javax.inject.Inject

class SearchNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(query: String): Flow<List<Note>> {
        val searchQuery = SearchNotesQuery(query)
        return notesRepository.searchNotesFlow(
            searchNotesQuery = searchQuery
        ).flowOn(ioDispatcher)
    }
}