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
import pl.atk.notes.TestData.Companion.TEST_NOTE_1
import pl.atk.notes.TestData.Companion.TEST_NOTE_1_UPDATED
import pl.atk.notes.TestData.Companion.TEST_NOTE_2
import pl.atk.notes.TestData.Companion.TEST_NOTE_3
import pl.atk.notes.TestData.Companion.TEST_NOTE_4
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.framework.db.NotesDatabase
import pl.atk.notes.utils.extensions.empty

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
    fun update_updateNote_success() = runTest {
        val note = TEST_NOTE_1
        notesDao.addNote(note)

        notesDao.update(note.copy(title = "Title1 - updated", content = "Content1 - updated"))

        notesDao.getNotesFlow().test {
            val list = awaitItem()
            Assert.assertEquals(TEST_NOTE_1_UPDATED, list[0])
            cancel()
        }
    }

    @Test
    fun update_updateNoteThatIsNotInDatabase_notExistingNoteNotInserted() = runTest {
        val note = TEST_NOTE_1
        val note2 = TEST_NOTE_2
        notesDao.addNote(note2)

        notesDao.update(note.copy(title = "Title1 - updated", content = "Content1 - updated"))

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

    @Test
    fun getNotesFlow_nullQuery_returnsWholeList() = runTest {
        val expectedList = listOf(TEST_NOTE_1, TEST_NOTE_2, TEST_NOTE_3)
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_2)
        notesDao.addNote(TEST_NOTE_3)

        notesDao.getNotesFlow().test {
            val list = awaitItem()
            Assert.assertEquals(expectedList, list)
            cancel()
        }
    }

    @Test
    fun getNotesFlow_emptyQuery_returnsWholeList() = runTest {
        val expectedList = listOf(TEST_NOTE_1, TEST_NOTE_2, TEST_NOTE_3)
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_2)
        notesDao.addNote(TEST_NOTE_3)

        notesDao.getNotesFlow(String.empty).test {
            val list = awaitItem()
            Assert.assertEquals(expectedList, list)
            cancel()
        }
    }

    @Test
    fun getNotesFlow_normalQueryByTitle_returnsGoodList() = runTest {
        val expectedList = listOf(TEST_NOTE_2, TEST_NOTE_3)
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_2)
        notesDao.addNote(TEST_NOTE_3)

        notesDao.getNotesFlow("Title2").test {
            val list = awaitItem()
            Assert.assertEquals(expectedList, list)
            cancel()
        }
    }

    @Test
    fun getNotesFlow_lowercaseQueryByTitle_returnsGoodList() = runTest {
        val expectedList = listOf(TEST_NOTE_2, TEST_NOTE_3)
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_2)
        notesDao.addNote(TEST_NOTE_3)

        notesDao.getNotesFlow("title2").test {
            val list = awaitItem()
            Assert.assertEquals(expectedList, list)
            cancel()
        }
    }

    @Test
    fun getNotesFlow_normalQueryByContent_returnsGoodList() = runTest {
        val expectedList = listOf(TEST_NOTE_2, TEST_NOTE_3)
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_2)
        notesDao.addNote(TEST_NOTE_3)

        notesDao.getNotesFlow("Content2").test {
            val list = awaitItem()
            Assert.assertEquals(expectedList, list)
            cancel()
        }
    }

    @Test
    fun getNotesFlow_lowercaseQueryByContent_returnsGoodList() = runTest {
        val expectedList = listOf(TEST_NOTE_2, TEST_NOTE_3)
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_2)
        notesDao.addNote(TEST_NOTE_3)

        notesDao.getNotesFlow("content2").test {
            val list = awaitItem()
            Assert.assertEquals(expectedList, list)
            cancel()
        }
    }

    @Test
    fun getNotesFlow_normalQueryByTitleAndContent_returnsGoodList() = runTest {
        val expectedList = listOf(TEST_NOTE_1, TEST_NOTE_4)
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_2)
        notesDao.addNote(TEST_NOTE_3)
        notesDao.addNote(TEST_NOTE_4)

        notesDao.getNotesFlow("Title1").test {
            val list = awaitItem()
            Assert.assertEquals(expectedList, list)
            cancel()
        }
    }

    @Test
    fun getNotesFlow_lowercaseQueryByTitleAndContent_returnsGoodList() = runTest {
        val expectedList = listOf(TEST_NOTE_1, TEST_NOTE_4)
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_2)
        notesDao.addNote(TEST_NOTE_3)
        notesDao.addNote(TEST_NOTE_4)

        notesDao.getNotesFlow("title1").test {
            val list = awaitItem()
            Assert.assertEquals(expectedList, list)
            cancel()
        }
    }

    @Test
    fun getNotesFlow_wrongQuery_returnsEmptyList() = runTest {
        notesDao.addNote(TEST_NOTE_1)
        notesDao.addNote(TEST_NOTE_2)
        notesDao.addNote(TEST_NOTE_3)

        notesDao.getNotesFlow("xyz").test {
            val list = awaitItem()
            Assert.assertEquals(0, list.size)
            cancel()
        }
    }
}