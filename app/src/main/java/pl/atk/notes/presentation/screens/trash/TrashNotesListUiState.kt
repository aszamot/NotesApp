package pl.atk.notes.presentation.screens.trash

import pl.atk.notes.presentation.model.NoteItemUi

data class TrashNotesListUiState(
    val notes: List<NoteItemUi> = listOf(),
    val notesEmpty: Boolean = false,
    val selectedNotesCount: Int = 0,
    val isInNoteSelectedMode: Boolean = false,
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val message: Int? = null
)