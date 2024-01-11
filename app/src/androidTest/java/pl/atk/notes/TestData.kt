package pl.atk.notes

import pl.atk.notes.framework.db.models.NoteEntity
import java.util.UUID

class TestData {

    companion object {
        val TEST_NOTE_1 = NoteEntity(
            id = UUID.fromString("a749d804-59a6-49b5-93d2-36429b45ec5b"),
            title = "Title1",
            content = "Content1",
            timestamp = 12345L,
            isInTrash = false,
            isArchived = false
        )
        val TEST_NOTE_1_UPDATED = NoteEntity(
            id = UUID.fromString("a749d804-59a6-49b5-93d2-36429b45ec5b"),
            title = "Title1 - updated",
            content = "Content1 - updated",
            timestamp = 12345L,
            isInTrash = false,
            isArchived = false
        )
        val TEST_NOTE_2 = NoteEntity(
            id = UUID.fromString("5cbb8277-e58d-4585-8b27-19725e358cf8"),
            title = "Title2",
            content = "Content2",
            timestamp = 12345L,
            isInTrash = false,
            isArchived = false
        )
        val TEST_NOTE_3 = NoteEntity(
            id = UUID.fromString("5f725107-a5d8-4698-8548-26e9b20dab6a"),
            title = "Title23",
            content = "Content23",
            timestamp = 12345L,
            isInTrash = false,
            isArchived = false
        )
        val TEST_NOTE_4 = NoteEntity(
            id = UUID.fromString("8f65b462-ee13-4c9f-85d0-1752fe43bd57"),
            title = "Title4",
            content = "Title1",
            timestamp = 12345L,
            isInTrash = false,
            isArchived = false
        )
    }

}