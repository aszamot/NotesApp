package pl.atk.notes.domain.usecases.delete

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.atk.notes.TestData
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.exceptions.NoteIsInTrashException
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.domain.repository.NotesRepository
import java.util.UUID

class DeleteNoteUseCaseTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var useCase: DeleteNoteUseCase
    private lateinit var repository: NotesRepository

    @Before
    fun setUp() {
        repository = mock()
        useCase = DeleteNoteUseCase(repository, dispatcherRule.testDispatcher)
    }

    @Test
    fun invoke_shouldCallRepositoryDeleteNote() = runTest {
        val noteId = UUID.randomUUID()

        useCase.invoke(noteId)

        verify(repository).deleteNote(noteId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun invoke_noteNotExisting_shouldThrowNoteNotFoundException() = runTest {
        val noteId = UUID.randomUUID()
        doAnswer { throw NoteNotFoundException() }.whenever(repository).deleteNote(noteId)

        useCase.invoke(noteId)
    }
}