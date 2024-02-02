package pl.atk.notes.domain.usecases.delete

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.repository.NotesRepository

class DeleteAllNotesInTrashUseCaseTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var useCase: DeleteAllNotesInTrashUseCase
    private lateinit var repository: NotesRepository

    @Before
    fun setUp() {
        repository = mock()
        useCase = DeleteAllNotesInTrashUseCase(repository, dispatcherRule.testDispatcher)
    }

    @Test
    fun invoke_shouldCallRepositoryDeleteAllNotesInTrash() = runTest {
        useCase.invoke()

        verify(repository).deleteAllNotesInTrash()
    }

}