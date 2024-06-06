package pl.atk.notes.domain.usecases

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.models.Note
import pl.atk.notes.domain.repository.NotesRepository

class AddEmptyNoteUseCaseTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var useCase: AddEmptyNoteUseCase
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        notesRepository = mock()
        useCase = AddEmptyNoteUseCase(notesRepository, dispatcherRule.testDispatcher)
    }

    @Test
    fun invoke_shouldCallNotesRepositoryAddNoteWithEmptyNote() = runTest {
        val noteCaptor = argumentCaptor<Note>()

        val noteId = useCase.invoke()

        verify(notesRepository).addNote(noteCaptor.capture())

        val capturedNote = noteCaptor.firstValue

        assertEquals(noteId, capturedNote.id)
        assertEquals("", capturedNote.title)
        assertEquals("", capturedNote.content)
        assertEquals(
            System.currentTimeMillis().toDouble(),
            capturedNote.timestamp.toDouble(),
            1000.0
        )
    }
}