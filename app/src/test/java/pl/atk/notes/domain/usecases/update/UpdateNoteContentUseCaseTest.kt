package pl.atk.notes.domain.usecases.update

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.domain.repository.NotesRepository
import java.util.UUID

class UpdateNoteContentUseCaseTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var useCase: UpdateNoteContentUseCase
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        notesRepository = mock()
        useCase = UpdateNoteContentUseCase(notesRepository, dispatcherRule.testDispatcher)
    }

    @Test
    fun invoke_shouldCallRepositoryUpdateContentTitle() = runTest {
        val noteId = UUID.randomUUID()
        val newContent = "New Content"

        useCase.invoke(noteId, newContent)

        verify(notesRepository).updateNoteContent(noteId, newContent)
    }

    @Test(expected = NoteNotFoundException::class)
    fun invoke_noteNotExisting_shouldThrowNoteNotFoundException() = runTest {
        val noteId = UUID.randomUUID()
        val newContent = "New Content"
        doAnswer { throw NoteNotFoundException() }.whenever(notesRepository)
            .updateNoteContent(noteId, newContent)

        useCase.invoke(noteId, newContent)
    }
}