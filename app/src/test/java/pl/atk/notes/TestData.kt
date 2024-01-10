package pl.atk.notes

import pl.atk.notes.domain.models.Note
import pl.atk.notes.framework.db.models.NoteEntity
import java.util.UUID

class TestData {

    companion object {
        val TEST_NOTE_ENTITY_1 = NoteEntity(
            id = UUID.fromString("a749d804-59a6-49b5-93d2-36429b45ec5b"),
            title = "Title1",
            content = "Content1",
            timestamp = 12345L,
            isInTrash = false,
            isArchived = false
        )
        val TEST_NOTE_ENTITY_2 = NoteEntity(
            id = UUID.fromString("5cbb8277-e58d-4585-8b27-19725e358cf8"),
            title = "Title2",
            content = "Content2",
            timestamp = 12345L,
            isInTrash = false,
            isArchived = false
        )
        val TEST_NOTE_1 = Note(
            id = UUID.fromString("a749d804-59a6-49b5-93d2-36429b45ec5b"),
            title = "Title1",
            content = "Content1",
            timestamp = 12345L,
            isInTrash = false,
            isArchived = false
        )
        val TEST_NOTE_2 = Note(
            id = UUID.fromString("5cbb8277-e58d-4585-8b27-19725e358cf8"),
            title = "Title2",
            content = "Content2",
            timestamp = 12345L,
            isInTrash = false,
            isArchived = false
        )
        val TEST_NOTE_1_DELETED = Note(
            id = UUID.fromString("a749d804-59a6-49b5-93d2-36429b45ec5b"),
            title = "Title1",
            content = "Content1",
            timestamp = 12345L,
            isInTrash = true,
            isArchived = false
        )
    }

}