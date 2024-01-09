package pl.atk.notes.domain.models

import java.util.UUID

data class Note(
    val id: UUID,
    val title: String?,
    val content: String?,
    val timestamp: Long,
    val isInTrash: Boolean,
    val isArchived: Boolean
)