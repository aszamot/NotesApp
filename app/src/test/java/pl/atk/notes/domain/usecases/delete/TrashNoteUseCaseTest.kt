package pl.atk.notes.domain.usecases.delete

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.atk.notes.TestData
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.exceptions.NoteIsInTrashException
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.domain.repository.NotesRepository
import java.util.UUID

class TrashNoteUseCaseTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var useCase: TrashNoteUseCase
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        notesRepository = mock()
        useCase = TrashNoteUseCase(notesRepository, dispatcherRule.testDispatcher)
    }

    @Test
    fun invoke_shouldCallNotesRepositoryTrashNote() = runTest {
        val noteId = UUID.randomUUID()

        useCase.invoke(noteId)

        verify(notesRepository).trashNote(noteId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun invoke_noteNotExisting_shouldThrowNoteNotFoundException() = runTest {
        val noteId = UUID.randomUUID()
        doAnswer { throw NoteNotFoundException() }.whenever(notesRepository).trashNote(noteId)

        useCase.invoke(noteId)
    }
}