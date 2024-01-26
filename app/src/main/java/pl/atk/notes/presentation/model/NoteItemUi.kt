package pl.atk.notes.presentation.model

import java.util.UUID

data class NoteItemUi(
    val id: UUID,
    val title: String?,
    val content: String?,
    var isSelected: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NoteItemUi) return false
        return id == other.id && title == other.title && content == other.content
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + content.hashCode()
        return result
    }
}