package pl.atk.notes.data.repositoryimpl

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.argThat
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.atk.notes.TestData
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.data.local.LocalNotesDataSource
import pl.atk.notes.domain.exceptions.NoteIsInTrashException
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.utils.FilterNotesByType
import pl.atk.notes.domain.utils.SearchNotesQuery
import java.util.UUID

class NotesRepositoryImplTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var repository: NotesRepositoryImpl
    private lateinit var localNotesDataSource: LocalNotesDataSource

    @Before
    fun setUp() {
        localNotesDataSource = mock()
        repository = NotesRepositoryImpl(localNotesDataSource, dispatcherRule.testDispatcher)
    }

    @Test
    fun getAllNotesFlow_shouldUseLocalNotesDataSourceForAllNotes() = runTest {
        repository.getAllNotesFlow(null)

        verify(localNotesDataSource).getNotesFlow()
    }

    @Test
    fun getAllNotesFlow_shouldUseLocalNotesDataSourceForArchivedNotes() = runTest {
        repository.getAllNotesFlow(FilterNotesByType.Archived)

        verify(localNotesDataSource).getArchivedNotesFlow()
    }

    @Test
    fun getAllNotesFlow_shouldUseLocalNotesDataSourceForNotesInTrash() = runTest {
        repository.getAllNotesFlow(FilterNotesByType.InTrash)

        verify(localNotesDataSource).getInTrashNotesFlow()
    }

    @Test
    fun getAllNotesFlow_shouldReturnNotes() = runTest {
        val notes = listOf(TestData.TEST_NOTE_1, TestData.TEST_NOTE_2)
        whenever(localNotesDataSource.getNotesFlow()).thenReturn(flowOf(notes))
        repository.getAllNotesFlow(null).test {
            val list = awaitItem()
            assertEquals(notes, list)
            awaitComplete()
        }
    }

    @Test
    fun getAllNotesFlow_withArchivedFilter_shouldReturnNotes() = runTest {
        val notes = listOf(TestData.TEST_NOTE_1, TestData.TEST_NOTE_2)
        whenever(localNotesDataSource.getArchivedNotesFlow()).thenReturn(flowOf(notes))
        repository.getAllNotesFlow(FilterNotesByType.Archived).test {
            val list = awaitItem()
            assertEquals(notes, list)
            awaitComplete()
        }
    }

    @Test
    fun getAllNotesFlow_withTrashFilter_shouldReturnNotes() = runTest {
        val notes = listOf(TestData.TEST_NOTE_1, TestData.TEST_NOTE_2)
        whenever(localNotesDataSource.getInTrashNotesFlow()).thenReturn(flowOf(notes))
        repository.getAllNotesFlow(FilterNotesByType.InTrash).test {
            val list = awaitItem()
            assertEquals(notes, list)
            awaitComplete()
        }
    }

    @Test
    fun searchNotesFlow_shouldUseLocalNotesDataSourceForSearch() = runTest {
        val query = "test"
        repository.searchNotesFlow(SearchNotesQuery(query), null)

        verify(localNotesDataSource).searchNotesFlow(query)
    }

    @Test
    fun searchNotesFlow_shouldUseLocalNotesDataSourceForArchivedNotesSearch() = runTest {
        val query = "test"
        repository.searchNotesFlow(SearchNotesQuery(query), FilterNotesByType.Archived)

        verify(localNotesDataSource).searchArchivedNotesFlow(query)
    }

    @Test
    fun searchNotesFlow_shouldUseLocalNotesDataSourceForNotesInTrashSearch() = runTest {
        val query = "test"
        repository.searchNotesFlow(SearchNotesQuery(query), FilterNotesByType.InTrash)

        verify(localNotesDataSource).getInTrashNotesFlow()
    }

    @Test
    fun searchNotesFlow_shouldReturnSearchedNotes() = runTest {
        val query = SearchNotesQuery("test")
        val notes = listOf(TestData.TEST_NOTE_1, TestData.TEST_NOTE_2)
        whenever(localNotesDataSource.searchNotesFlow(query.query)).thenReturn(flowOf(notes))

        repository.searchNotesFlow(query, null).test {
            val list = awaitItem()
            assertEquals(notes, list)
            awaitComplete()
        }
    }

    @Test
    fun searchNotesFlow_withArchivedFilter_shouldReturnSearchedNotes() = runTest {
        val query = SearchNotesQuery("test")
        val notes = listOf(TestData.TEST_NOTE_1, TestData.TEST_NOTE_2)
        whenever(localNotesDataSource.searchArchivedNotesFlow(query.query)).thenReturn(flowOf(notes))

        repository.searchNotesFlow(query, FilterNotesByType.Archived).test {
            val list = awaitItem()
            assertEquals(notes, list)
            awaitComplete()
        }
    }

    @Test
    fun searchNotesFlow_withTrashFilter_shouldReturnSearchedNotes() = runTest {
        val query = SearchNotesQuery("test")
        val notes = listOf(TestData.TEST_NOTE_1, TestData.TEST_NOTE_2)
        whenever(localNotesDataSource.getInTrashNotesFlow()).thenReturn(flowOf(notes))

        repository.searchNotesFlow(query, FilterNotesByType.InTrash).test {
            val list = awaitItem()
            assertEquals(notes, list)
            awaitComplete()
        }
    }

    @Test
    fun addNote_shouldCallLocalDataSourceAddNote() = runTest {
        val note = TestData.TEST_NOTE_1

        repository.addNote(note)

        verify(localNotesDataSource).addNote(note)
    }

    @Test
    fun archiveNote_shouldCallLocalDataSourceArchiveNote() = runTest {
        val noteId = UUID.randomUUID()

        repository.archiveNote(noteId)

        verify(localNotesDataSource).archiveNote(noteId)
    }

    @Test(expected = NoteIsInTrashException::class)
    fun archiveNote_noteInTrash_shouldThrowNoteIsInTrashException() = runTest {
        val noteId = UUID.randomUUID()
        doAnswer { throw NoteIsInTrashException() }.whenever(localNotesDataSource).archiveNote(noteId)

        repository.archiveNote(noteId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun archiveNote_noteNotFound_shouldThrowNoteNotFoundException() = runTest {
        val noteId = UUID.randomUUID()
        doAnswer { throw NoteNotFoundException() }.whenever(localNotesDataSource).archiveNote(noteId)

        repository.archiveNote(noteId)
    }

    @Test
    fun unArchiveNote_shouldCallLocalDataSourceUnArchiveNote() = runTest {
        val noteId = UUID.randomUUID()

        repository.unArchiveNote(noteId)

        verify(localNotesDataSource).unArchiveNote(noteId)
    }

    @Test(expected = NoteIsInTrashException::class)
    fun unArchiveNote_noteInTrash_shouldThrowNoteIsInTrashException() = runTest {
        val noteId = UUID.randomUUID()
        doAnswer { throw NoteIsInTrashException() }.whenever(localNotesDataSource).unArchiveNote(noteId)

        repository.unArchiveNote(noteId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun unarchiveNote_noteNotFound_shouldThrowNoteNotFoundException() = runTest {
        val noteId = UUID.randomUUID()
        doAnswer { throw NoteNotFoundException() }.whenever(localNotesDataSource).unArchiveNote(noteId)

        repository.unArchiveNote(noteId)
    }

    @Test
    fun trashNote_shouldCallLocalDataSourceTrashNote() = runTest {
        val noteId = UUID.randomUUID()

        repository.trashNote(noteId)

        verify(localNotesDataSource).trashNote(noteId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun trashNote_noteNotFound_shouldThrowNoteNotFoundException() = runTest {
        val noteId = UUID.randomUUID()
        doAnswer { throw NoteNotFoundException() }.whenever(localNotesDataSource).trashNote(noteId)

        repository.trashNote(noteId)
    }

    @Test
    fun unTrashNote_shouldCallLocalDataSourceUnTrashNote() = runTest {
        val noteId = UUID.randomUUID()

        repository.unTrashNote(noteId)

        verify(localNotesDataSource).unTrashNote(noteId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun unTrashNote_noteNotFound_shouldThrowNoteNotFoundException() = runTest {
        val noteId = UUID.randomUUID()
        doAnswer { throw NoteNotFoundException() }.whenever(localNotesDataSource).unTrashNote(noteId)

        repository.unTrashNote(noteId)
    }

    @Test
    fun deleteNote_shouldCallLocalDataSourceDeleteNote() = runTest {
        val noteId = UUID.randomUUID()

        repository.deleteNote(noteId)

        verify(localNotesDataSource).deleteNote(noteId)
    }
}