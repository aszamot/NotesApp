package pl.atk.notes.utils.extensions

import pl.atk.notes.domain.models.Note
import pl.atk.notes.framework.db.models.NoteEntity

fun Note.toNoteEntity() = NoteEntity(
    id = this.id,
    title = this.title,
    content = this.content,
    timestamp = this.timestamp,
    isInTrash = this.isInTrash,
    isArchived = this.isArchived
)

fun NoteEntity.toNote() = Note(
    id = this.id,
    title = this.title,
    content = this.content,
    timestamp = this.timestamp,
    isInTrash = this.isInTrash,
    isArchived = this.isArchived
)