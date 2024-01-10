package pl.atk.notes.domain.usecases

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import pl.atk.notes.TestData
import pl.atk.notes.domain.repository.NotesRepository

class GetNotesFlowUseCaseTest {

    private lateinit var useCase: GetNotesFlowUseCase
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        notesRepository = mock()
        useCase = GetNotesFlowUseCase(notesRepository)
    }

    @Test
    fun invoke_shouldReturnNotesFromNotesRepository() = runTest {
        val notes = listOf(TestData.TEST_NOTE_1, TestData.TEST_NOTE_2)
        whenever(notesRepository.getNotesFlow()).thenReturn(flowOf(notes))

        useCase.invoke().test {
            val list = awaitItem()
            Assert.assertEquals(notes, list)
            awaitComplete()
        }
    }
}