package pl.atk.notes.data.repositoryimpl

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.atk.notes.TestData
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.data.local.LocalNotesDataSource
import pl.atk.notes.domain.exceptions.NoteNotFoundException
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
        repository.getAllNotesFlow()

        verify(localNotesDataSource).getNotesFlow()
    }

    @Test
    fun getAllNotesFlow_shouldReturnNotes() = runTest {
        val notes = listOf(TestData.TEST_NOTE_1, TestData.TEST_NOTE_2)
        whenever(localNotesDataSource.getNotesFlow()).thenReturn(flowOf(notes))
        repository.getAllNotesFlow().test {
            val list = awaitItem()
            assertEquals(notes, list)
            awaitComplete()
        }
    }

    @Test
    fun searchNotesFlow_shouldUseLocalNotesDataSourceForSearch() = runTest {
        val query = "test"
        repository.searchNotesFlow(SearchNotesQuery(query))

        verify(localNotesDataSource).searchNotesFlow(query)
    }

    @Test
    fun searchNotesFlow_shouldReturnSearchedNotes() = runTest {
        val query = SearchNotesQuery("test")
        val notes = listOf(TestData.TEST_NOTE_1, TestData.TEST_NOTE_2)
        whenever(localNotesDataSource.searchNotesFlow(query.query)).thenReturn(flowOf(notes))

        repository.searchNotesFlow(query).test {
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
    fun updateNoteTitle_shouldCallLocalDataSourceUpdateNoteTitle() = runTest {
        val noteId = UUID.randomUUID()
        val newTitle = "newTitle"

        repository.updateNoteTitle(noteId, newTitle)

        verify(localNotesDataSource).updateNoteTitle(noteId, newTitle)
    }

    @Test(expected = NoteNotFoundException::class)
    fun updateNoteTitle_noteNotFound_shouldThrowNoteNotFoundException() = runTest {
        val noteId = UUID.randomUUID()
        val newTitle = "newTitle"

        doAnswer { throw NoteNotFoundException() }.whenever(localNotesDataSource)
            .updateNoteTitle(noteId, newTitle)

        repository.updateNoteTitle(noteId, newTitle)
    }

    @Test
    fun updateNoteContent_shouldCallLocalDataSourceUpdateNoteContent() = runTest {
        val noteId = UUID.randomUUID()
        val newContent = "New content"

        repository.updateNoteContent(noteId, newContent)

        verify(localNotesDataSource).updateNoteContent(noteId, newContent)
    }

    @Test(expected = NoteNotFoundException::class)
    fun updateNoteContent_noteNotFound_shouldThrowNoteNotFoundException() = runTest {
        val noteId = UUID.randomUUID()
        val newContent = "New content"

        doAnswer { throw NoteNotFoundException() }.whenever(localNotesDataSource)
            .updateNoteContent(noteId, newContent)

        repository.updateNoteContent(noteId, newContent)
    }

    @Test
    fun deleteNote_shouldCallLocalDataSourceDeleteNote() = runTest {
        val noteId = UUID.randomUUID()

        repository.deleteNote(noteId)

        verify(localNotesDataSource).deleteNote(noteId)
    }
}