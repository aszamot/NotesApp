package pl.atk.notes.presentation.screens.details

import java.util.Date

data class NoteDetailsUiState(
    val noteTitle: String? = null,
    val noteContent: String? = null,
    val lastChangedDate: Date? = null,
    val error: Throwable? = null,
    val message: Int? = null,
    val navigateBack: Boolean = false
)