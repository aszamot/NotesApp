package pl.atk.notes.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.atk.notes.TestData

class NotesExtensionsKtTest {

    @Test
    fun note_toNoteEntity_success() {
        val noteEntity = TestData.TEST_NOTE_1.toNoteEntity()

        assertEquals(TestData.TEST_NOTE_ENTITY_1, noteEntity)
    }

    @Test
    fun noteEntity_toNote_success() {
        val note = TestData.TEST_NOTE_ENTITY_1.toNote()

        assertEquals(TestData.TEST_NOTE_1, note)
    }
}