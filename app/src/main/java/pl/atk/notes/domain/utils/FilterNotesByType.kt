package pl.atk.notes.domain.utils

sealed class FilterNotesByType {
    data object Archived : FilterNotesByType()
    data object InTrash : FilterNotesByType()
}