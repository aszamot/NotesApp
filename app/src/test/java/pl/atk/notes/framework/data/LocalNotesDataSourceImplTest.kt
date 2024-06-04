package pl.atk.notes.framework.data

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.atk.notes.TestData
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.framework.db.daos.NotesDao
import pl.atk.notes.utils.extensions.toNote
import pl.atk.notes.utils.extensions.toNoteEntity
import java.util.UUID

class LocalNotesDataSourceImplTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var dataSource: LocalNotesDataSourceImpl
    private lateinit var notesDao: NotesDao

    @Before
    fun setUp() {
        notesDao = mock()
        dataSource = LocalNotesDataSourceImpl(notesDao, dispatcherRule.testDispatcher)
    }

    @Test
    fun addNote_shouldCallDaoAddNote() = runTest {
        val note = TestData.TEST_NOTE_1
        dataSource.addNote(note)

        verify(notesDao).addNote(note.toNoteEntity())
    }

    @Test
    fun deleteNote_shouldCallDaoDeleteNote() = runTest {
        val noteId = UUID.randomUUID()
        dataSource.deleteNote(noteId)

        verify(notesDao).delete(noteId)
    }

    @Test
    fun searchNotesFlow_shouldReturnNotes() = runTest {
        val query = "test"
        val noteEntities =
            listOf(TestData.TEST_NOTE_1.toNoteEntity(), TestData.TEST_NOTE_2.toNoteEntity())

        whenever(notesDao.searchNotesFlow(query)).thenReturn(flowOf(noteEntities))

        dataSource.searchNotesFlow(query).test {
            val list = awaitItem()
            Assert.assertEquals(noteEntities.map { it.toNote() }, list)
            awaitComplete()
        }
    }

    @Test
    fun getNotesFlow_shouldReturnNotes() = runTest {
        val noteEntities =
            listOf(TestData.TEST_NOTE_1.toNoteEntity(), TestData.TEST_NOTE_2.toNoteEntity())

        whenever(notesDao.getAllNotesFlow()).thenReturn(flowOf(noteEntities))

        dataSource.getNotesFlow().test {
            val list = awaitItem()
            Assert.assertEquals(noteEntities.map { it.toNote() }, list)
            awaitComplete()
        }
    }
}