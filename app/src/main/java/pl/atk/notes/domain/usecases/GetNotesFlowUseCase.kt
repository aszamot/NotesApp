package pl.atk.notes.domain.usecases

import pl.atk.notes.domain.repository.NotesRepository

class GetNotesFlowUseCase(
    private val notesRepository: NotesRepository
) {
    fun invoke(query: String? = null) = notesRepository.getNotesFlow(query)
}