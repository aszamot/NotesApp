package pl.atk.notes.data.repositoryimpl

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.atk.notes.TestData
import pl.atk.notes.data.local.LocalNotesDataSource

class NotesRepositoryImplTest {

    private lateinit var repository: NotesRepositoryImpl
    private lateinit var localNotesDataSource: LocalNotesDataSource

    @Before
    fun setUp() {
        localNotesDataSource = mock()
        repository = NotesRepositoryImpl(localNotesDataSource)
    }

    @Test
    fun addNote_shouldCallLocalDataStoreAddNote() = runTest {
        val note = TestData.TEST_NOTE_1

        repository.addNote(note)

        verify(localNotesDataSource).addNote(note)
    }

    @Test
    fun deleteNote_shouldCallLocalDataStoreDeleteNote() = runTest {
        val note = TestData.TEST_NOTE_1

        repository.deleteNote(note)

        verify(localNotesDataSource).deleteNote(note)
    }

    @Test
    fun getNotes_shouldCallLocalDataStoreDeleteNote() = runTest {
        val notes = listOf(TestData.TEST_NOTE_1, TestData.TEST_NOTE_2)
        whenever(localNotesDataSource.getNotesFlow()).thenReturn(flowOf(notes))

        repository.getNotesFlow().test {
            val list = awaitItem()
            assertEquals(notes, list)
            awaitComplete()
        }
    }
}