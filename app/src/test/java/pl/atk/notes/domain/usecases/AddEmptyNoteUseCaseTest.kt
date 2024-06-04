package pl.atk.notes.domain.usecases

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import pl.atk.notes.TestData
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.repository.NotesRepository

//todo
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
    fun invoke_shouldCallNotesRepositoryAddNote() = runTest {
        val note = TestData.TEST_NOTE_1

        useCase.invoke()

        verify(notesRepository).addNote(note)
    }
}