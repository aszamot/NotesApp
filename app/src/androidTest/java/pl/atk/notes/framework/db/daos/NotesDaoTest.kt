package pl.atk.notes.framework.db.daos

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.atk.notes.TestData
import pl.atk.notes.TestData.Companion.TEST_NOTE_1
import pl.atk.notes.TestData.Companion.TEST_NOTE_2
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.framework.db.NotesDatabase

@RunWith(AndroidJUnit4::class)
@SmallTest
class NotesDaoTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var db: NotesDatabase
    private lateinit var notesDao: NotesDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NotesDatabase::class.java
        ).build()

        notesDao = db.notesDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun addNote_shouldInsertNote() = runTest {
        val noteEntity = TEST_NOTE_1

        notesDao.addNote(noteEntity)

        val loaded = notesDao.getNote(noteEntity.id)
        assertEquals(noteEntity, loaded)
    }

    @Test
    fun updateNote_shouldUpdateNote() = runTest {
        val originalNoteEntity = TEST_NOTE_1
        notesDao.addNote(originalNoteEntity)

        val updatedNoteEntity = originalNoteEntity.copy(title = "Updated Title")
        notesDao.update(updatedNoteEntity)

        val loaded = notesDao.getNote(originalNoteEntity.id)
        assertEquals(updatedNoteEntity, loaded)
    }

    @Test
    fun deleteNote_shouldDeleteNote() = runTest {
        val noteEntity = TEST_NOTE_1
        notesDao.addNote(noteEntity)

        notesDao.delete(noteEntity.id)

        val loaded = notesDao.getNote(noteEntity.id)
        assertNull(loaded)
    }

    @Test
    fun deleteAllNotesInTrash_shouldDeleteAllNotesInTrash() = runTest {
        val notesEntities = listOf(TEST_NOTE_1, TEST_NOTE_2.copy(isInTrash = true))
        notesEntities.forEach { notesDao.addNote(it) }

        notesDao.deleteAllNotesInTrash()
        notesDao.getInTrashNotesFlow().test {
            val list = awaitItem()
            assertEquals(0, list.size)
            cancel()
        }
    }

    @Test
    fun getAllNotesFlow_shouldReturnAllNotes() = runTest {
        val noteEntities = listOf(TEST_NOTE_1, TEST_NOTE_2)
        noteEntities.forEach { notesDao.addNote(it) }

        notesDao.getAllNotesFlow().test {
            val list = awaitItem()
            assertEquals(noteEntities, list)
            cancel()
        }
    }

    @Test
    fun searchNotesFlow_shouldReturnSearchedNotes() = runTest {
        val query = "test"
        val noteEntities = listOf(
            TEST_NOTE_1.copy(title = "Test 1"),
            TEST_NOTE_2.copy(title = "Test 2")
        )
        noteEntities.forEach { notesDao.addNote(it) }

        notesDao.searchNotesFlow(query).test {
            val list = awaitItem()
            assertEquals(noteEntities, list)
            cancel()
        }
    }

    @Test
    fun getArchivedNotesFlow_shouldReturnSearchedArchivedNotes() = runTest {
        val query = "test"
        val noteEntities = listOf(
            TEST_NOTE_1.copy(isArchived = true, title = "Test 1"),
            TEST_NOTE_2.copy(isArchived = true, title = "Test 1")
        )
        noteEntities.forEach { notesDao.addNote(it) }

        notesDao.searchArchivedNotesFlow(query).test {
            val list = awaitItem()
            assertEquals(noteEntities, list)
            cancel()
        }
    }

    @Test
    fun getArchivedNotesFlow_shouldReturnArchivedNotes() = runTest {
        val noteEntities = listOf(
            TEST_NOTE_1.copy(isArchived = true),
            TEST_NOTE_2.copy(isArchived = true)
        )
        noteEntities.forEach { notesDao.addNote(it) }

        notesDao.getArchivedNotesFlow().test {
            val list = awaitItem()
            assertEquals(noteEntities, list)
            cancel()
        }
    }

    @Test
    fun getInTrashNotesFlow_shouldReturnNotesInTrash() = runTest {
        val noteEntities = listOf(
            TEST_NOTE_1.copy(isInTrash = true),
            TEST_NOTE_2.copy(isInTrash = true)
        )
        noteEntities.forEach { notesDao.addNote(it) }

        notesDao.getInTrashNotesFlow().test {
            val list = awaitItem()
            assertEquals(noteEntities, list)
            cancel()
        }
    }

}