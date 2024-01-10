package pl.atk.notes.domain.usecases.delete

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import pl.atk.notes.TestData
import pl.atk.notes.domain.repository.NotesRepository

class DeleteNoteUseCaseTest {

    private lateinit var useCase: DeleteNoteUseCase
    private lateinit var repository: NotesRepository

    @Before
    fun setUp() {
        repository = mock()
        useCase = DeleteNoteUseCase(repository)
    }

    @Test
    fun invoke_shouldCallRepositoryDeleteNote() = runTest {
        val note = TestData.TEST_NOTE_1
        useCase.invoke(note)

        verify(repository).deleteNote(note)
    }

    @Test
    fun multipleInvoke_shouldCallRepositoryDeleteNoteMultipleTimes() = runTest {
        val note = TestData.TEST_NOTE_1

        useCase.invoke(note)
        useCase.invoke(note)
        useCase.invoke(note)

        verify(repository, times(3)).deleteNote(note)
    }
}