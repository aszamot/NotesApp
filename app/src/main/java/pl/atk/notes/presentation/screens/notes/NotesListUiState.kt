package pl.atk.notes.presentation.screens.notes

import pl.atk.notes.presentation.model.NoteItemUi
import java.util.UUID

data class NotesListUiState(
    val notes: List<NoteItemUi> = listOf(),
    val notesEmpty: Boolean = false,
    val searchedNotes: List<NoteItemUi> = listOf(),
    val searchedNotesEmpty: Boolean = false,
    val searchQueryEmpty: Boolean = true,
    val isInSearch: Boolean = false,
    val selectedNotesCount: Int = 0,
    val isInNoteSelectedMode: Boolean = false,
    val noteToGoToId: UUID? = null,
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val message: Int? = null
)