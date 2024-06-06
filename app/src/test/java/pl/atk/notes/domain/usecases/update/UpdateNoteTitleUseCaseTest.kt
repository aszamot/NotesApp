package pl.atk.notes.domain.usecases.update

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.domain.repository.NotesRepository
import pl.atk.notes.domain.usecases.AddEmptyNoteUseCase
import java.util.UUID

class UpdateNoteTitleUseCaseTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var useCase: UpdateNoteTitleUseCase
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        notesRepository = mock()
        useCase = UpdateNoteTitleUseCase(notesRepository, dispatcherRule.testDispatcher)
    }

    @Test
    fun invoke_shouldCallRepositoryUpdateNoteTitle() = runTest {
        val noteId = UUID.randomUUID()
        val newTitle = "New Title"

        useCase.invoke(noteId, newTitle)

        verify(notesRepository).updateNoteTitle(noteId, newTitle)
    }

    @Test(expected = NoteNotFoundException::class)
    fun invoke_noteNotExisting_shouldThrowNoteNotFoundException() = runTest {
        val noteId = UUID.randomUUID()
        val newTitle = "New Title"
        doAnswer { throw NoteNotFoundException() }.whenever(notesRepository)
            .updateNoteTitle(noteId, newTitle)

        useCase.invoke(noteId, newTitle)
    }

}