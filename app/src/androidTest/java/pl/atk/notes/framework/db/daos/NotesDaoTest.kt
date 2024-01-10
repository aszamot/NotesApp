package pl.atk.notes.framework.db.daos

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.framework.db.NotesDatabase
import pl.atk.notes.framework.db.models.NoteEntity
import java.util.UUID

@RunWith(AndroidJUnit4::class)
@SmallTest
class NotesDaoTest {

    companion object {
        private val TEST_NOTE_1 = NoteEntity(
            id = UUID.fromString("a749d804-59a6-49b5-93d2-36429b45ec5b"),
            title = "Title1",
            content = "Content1",
            timestamp = 12345L,
            isInTrash = false,
            isArchived = false
        )
        private val TEST_NOTE_2 = NoteEntity(
            id = UUID.fromString("5cbb8277-e58d-4585-8b27-19725e358cf8"),
            title = "Title2",
            content = "Content2",
            timestamp = 12345L,
            isInTrash = false,
            isArchived = false
        )
    }

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
    fun add_addNote_success() = runTest {
        notesDao.addNote(TEST_NOTE_1)

        notesDao.getNotesFlow().test {
            val list = awaitItem()
            Assert.assertEquals(TEST_NOTE_1, list[0])
            cancel()
        }
    }

    @Test
    fun add_addNote_databaseListSizeCorrect() = runTest {
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_2)

        notesDao.getNotesFlow().test {
            val list = awaitItem()
            Assert.assertEquals(2, list.size)
            cancel()
        }
    }

    @Test
    fun add_addNoteOnConflict_success() = runTest {
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_1)

        notesDao.getNotesFlow().test {
            val list = awaitItem()
            Assert.assertEquals(TEST_NOTE_1, list[0])
            cancel()
        }
    }

    @Test
    fun add_addNoteOnConflict_databaseListSizeCorrect() = runTest {
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_1)

        notesDao.getNotesFlow().test {
            val list = awaitItem()
            Assert.assertEquals(1, list.size)
            cancel()
        }
    }

    @Test
    fun delete_deletesNote_databaseListSizeCorrect() = runTest {
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_2)

        notesDao.delete(TEST_NOTE_1)

        notesDao.getNotesFlow().test {
            val list = awaitItem()
            Assert.assertEquals(1, list.size)
            cancel()
        }
    }
    @Test
    fun delete_deletesNote_success() = runTest {
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_2)

        notesDao.delete(TEST_NOTE_1)

        notesDao.getNotesFlow().test {
            val list = awaitItem()
            Assert.assertEquals(TEST_NOTE_2, list[0])
            cancel()
        }
    }

    @Test
    fun delete_deleteNoteThatIsNotInDatabase_success() = runTest {
        notesDao.addNote(TEST_NOTE_1)

        notesDao.delete(TEST_NOTE_2)

        notesDao.getNotesFlow().test {
            val list = awaitItem()
            Assert.assertEquals(1, list.size)
            cancel()
        }
    }

}