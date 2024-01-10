package pl.atk.notes.framework.data

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
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
import pl.atk.notes.utils.toNote
import pl.atk.notes.utils.toNoteEntity

class LocalNotesDataSourceImplTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var dataSource: LocalNotesDataSourceImpl
    private lateinit var notesDao: NotesDao

    @Before
    fun setUp() {
        notesDao = mock()
        dataSource = LocalNotesDataSourceImpl(notesDao)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun addNote_shouldCallDaoAddNote() = runTest {
        val note = TestData.TEST_NOTE_1
        dataSource.addNote(note)

        verify(notesDao).addNote(note.toNoteEntity())
    }

    @Test
    fun updateNote_shouldCallDaoUpdate() = runTest {
        val note = TestData.TEST_NOTE_1
        dataSource.updateNote(note)

        verify(notesDao).update(note.toNoteEntity())
    }

    @Test
    fun deleteNote_shouldCallDaoDeleteNote() = runTest {
        val note = TestData.TEST_NOTE_1
        dataSource.deleteNote(TestData.TEST_NOTE_1)

        verify(notesDao).delete(note.toNoteEntity())
    }

    @Test
    fun getNotesFlow_shouldReturnMappedNotes() = runTest {
        val notesEntities = listOf(TestData.TEST_NOTE_ENTITY_1, TestData.TEST_NOTE_ENTITY_2)
        val expectedNotes = notesEntities.map { it.toNote() }
        whenever(notesDao.getNotesFlow()).thenReturn(flowOf(notesEntities))

        dataSource.getNotesFlow().test {
            val list = awaitItem()
            Assert.assertEquals(expectedNotes, list)
            awaitComplete()
        }
    }
}