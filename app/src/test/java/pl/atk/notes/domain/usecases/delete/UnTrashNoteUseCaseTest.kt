package pl.atk.notes.domain.usecases.delete

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import pl.atk.notes.TestData
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.repository.NotesRepository

class UnTrashNoteUseCaseTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var useCase: UnTrashNoteUseCase
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        notesRepository = mock()
        useCase = UnTrashNoteUseCase(notesRepository)
    }

    @Test
    fun invoke_shouldCallNotesRepositoryUnTrashNote() = runTest {
        val note = TestData.TEST_NOTE_1

        useCase.invoke(note)

        verify(notesRepository).unTrashNote(note)
    }

}