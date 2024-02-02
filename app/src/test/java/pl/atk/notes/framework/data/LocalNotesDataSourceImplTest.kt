package pl.atk.notes.framework.data

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.atk.notes.TestData
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.exceptions.NoteIsInTrashException
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.framework.db.daos.NotesDao
import pl.atk.notes.utils.extensions.empty
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
    fun archiveNote_shouldCallNotesDaoUpdate() = runTest {
        val noteId = UUID.randomUUID()
        val note = TestData.TEST_NOTE_1

        whenever(notesDao.getNote(noteId)).thenReturn(note.toNoteEntity())
        dataSource.archiveNote(noteId)

        verify(notesDao).update(any())
    }

    @Test(expected = NoteIsInTrashException::class)
    fun archiveNote_whenNoteIsInTrash_shouldThrowException() = runTest {
        val noteId = UUID.randomUUID()
        val note = TestData.TEST_NOTE_1.copy(isInTrash = true)

        whenever(notesDao.getNote(noteId)).thenReturn(note.toNoteEntity())
        dataSource.archiveNote(noteId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun archiveNote_whenNoteNotFound_shouldThrowException() = runTest {
        val noteId = UUID.randomUUID()

        whenever(notesDao.getNote(noteId)).thenReturn(null)
        dataSource.archiveNote(noteId)
    }

    @Test
    fun unArchiveNote_shouldCallNotesDaoUpdate() = runTest {
        val noteId = UUID.randomUUID()
        val note = TestData.TEST_NOTE_1

        whenever(notesDao.getNote(noteId)).thenReturn(note.toNoteEntity())
        dataSource.unArchiveNote(noteId)

        verify(notesDao).update(any())
    }

    @Test(expected = NoteIsInTrashException::class)
    fun unArchiveNote_whenNoteIsInTrash_shouldThrowException() = runTest {
        val noteId = UUID.randomUUID()
        val note = TestData.TEST_NOTE_1.copy(isInTrash = true)

        whenever(notesDao.getNote(noteId)).thenReturn(note.toNoteEntity())
        dataSource.unArchiveNote(noteId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun unArchiveNote_whenNoteNotFound_shouldThrowException() = runTest {
        val noteId = UUID.randomUUID()

        whenever(notesDao.getNote(noteId)).thenReturn(null)
        dataSource.unArchiveNote(noteId)
    }

    @Test
    fun trashNote_shouldCallNotesDaoUpdate() = runTest {
        val noteId = UUID.randomUUID()
        val note = TestData.TEST_NOTE_1

        whenever(notesDao.getNote(noteId)).thenReturn(note.toNoteEntity())
        dataSource.trashNote(noteId)

        verify(notesDao).update(any())
    }

    @Test(expected = NoteNotFoundException::class)
    fun trashNote_whenNoteNotFound_shouldThrowException() = runTest {
        val noteId = UUID.randomUUID()

        whenever(notesDao.getNote(noteId)).thenReturn(null)
        dataSource.trashNote(noteId)
    }

    @Test
    fun unTrashNote_shouldCallNotesDaoUpdate() = runTest {
        val noteId = UUID.randomUUID()
        val note = TestData.TEST_NOTE_1

        whenever(notesDao.getNote(noteId)).thenReturn(note.toNoteEntity())
        dataSource.unTrashNote(noteId)

        verify(notesDao).update(any())
    }

    @Test(expected = NoteNotFoundException::class)
    fun unTrashNote_whenNoteNotFound_shouldThrowException() = runTest {
        val noteId = UUID.randomUUID()

        whenever(notesDao.getNote(noteId)).thenReturn(null)
        dataSource.unTrashNote(noteId)
    }

    @Test
    fun deleteNote_shouldCallDaoDeleteNote() = runTest {
        val noteId = UUID.randomUUID()
        dataSource.deleteNote(noteId)

        verify(notesDao).delete(noteId)
    }

    @Test
    fun deleteAllNotesInTrash_shouldCallDaoDeleteAllNotesInTrash() = runTest {
        dataSource.deleteAllNotesInTrash()

        verify(notesDao).deleteAllNotesInTrash()
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

    @Test
    fun getArchivedNotesFlow_shouldReturnNotes() = runTest {
        val noteEntities =
            listOf(TestData.TEST_NOTE_1.toNoteEntity(), TestData.TEST_NOTE_2.toNoteEntity())

        whenever(notesDao.getArchivedNotesFlow()).thenReturn(flowOf(noteEntities))

        dataSource.getArchivedNotesFlow().test {
            val list = awaitItem()
            Assert.assertEquals(noteEntities.map { it.toNote() }, list)
            awaitComplete()
        }
    }

    @Test
    fun searchArchivedNotesFlow_shouldReturnNotes() = runTest {
        val query = "test"
        val noteEntities =
            listOf(TestData.TEST_NOTE_1.toNoteEntity(), TestData.TEST_NOTE_2.toNoteEntity())

        whenever(notesDao.searchArchivedNotesFlow(query)).thenReturn(flowOf(noteEntities))

        dataSource.searchArchivedNotesFlow(query).test {
            val list = awaitItem()
            Assert.assertEquals(noteEntities.map { it.toNote() }, list)
            awaitComplete()
        }
    }

    @Test
    fun getInTrashNotesFlow_shouldReturnNotes() = runTest {
        val noteEntities =
            listOf(TestData.TEST_NOTE_1.toNoteEntity(), TestData.TEST_NOTE_2.toNoteEntity())

        whenever(notesDao.getInTrashNotesFlow()).thenReturn(flowOf(noteEntities))

        dataSource.getInTrashNotesFlow().test {
            val list = awaitItem()
            Assert.assertEquals(noteEntities.map { it.toNote() }, list)
            awaitComplete()
        }
    }
}