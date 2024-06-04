package pl.atk.notes.presentation.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.atk.notes.R
import pl.atk.notes.domain.usecases.AddEmptyNoteUseCase
import pl.atk.notes.domain.usecases.GetNoteUseCase
import pl.atk.notes.domain.usecases.delete.DeleteNoteUseCase
import pl.atk.notes.domain.usecases.update.UpdateNoteContentUseCase
import pl.atk.notes.domain.usecases.update.UpdateNoteTitleUseCase
import pl.atk.notes.utils.extensions.toDate
import java.util.UUID
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getNoteUseCase: GetNoteUseCase,
    private val addEmptyNoteUseCase: AddEmptyNoteUseCase,
    private val updateNoteTitleUseCase: UpdateNoteTitleUseCase,
    private val updateNoteContentUseCase: UpdateNoteContentUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
) : ViewModel() {

    companion object {
        private const val NOTE_ID_KEY = "noteId"
    }

    private var noteId = state.get<UUID?>(NOTE_ID_KEY)

    private val _noteTitleInput = MutableStateFlow<String?>(null)
    private val _noteContentInput = MutableStateFlow<String?>(null)

    private val _uiState = MutableStateFlow(NoteDetailsUiState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        NoteDetailsUiState()
    )

    init {
        observeTitleInputFlow()
        observeContentInputFlow()
        createOrObserveNote()
    }

    private fun createOrObserveNote() {
        if (noteId == null) {
            createAndObserveNote()
        } else {
            observeNote()
        }
    }

    private fun createAndObserveNote() {
        viewModelScope.launch {
            val emptyNoteId = addEmptyNoteUseCase.invoke()
            noteId = emptyNoteId
            state[NOTE_ID_KEY] = emptyNoteId
            observeNote()
        }
    }

    private fun observeNote() {
        getNoteUseCase.invoke(noteId)
            .map { note ->
                _uiState.value = _uiState.value.copy(
                    noteTitle = note?.title,
                    noteContent = note?.content,
                    lastChangedDate = note?.timestamp?.toDate()
                )
            }.catch { e ->
                setError(e)
            }.launchIn(viewModelScope)
    }

    private fun observeTitleInputFlow() {
        _noteTitleInput
            .debounce(500)
            .filterNotNull()
            .map { title ->
                noteId?.let {
                    updateNoteTitleUseCase.invoke(it, title)
                }
            }
            .catch { e ->
                setError(e)
            }
            .launchIn(viewModelScope)
    }

    fun updateTitle(title: String) {
        _noteTitleInput.value = title
    }

    private fun observeContentInputFlow() {
        _noteContentInput
            .debounce(500)
            .filterNotNull()
            .map { content ->
                noteId?.let {
                    updateNoteContentUseCase.invoke(it, content)
                }
            }
            .catch { e ->
                setError(e)
            }
            .launchIn(viewModelScope)
    }

    fun updateContent(content: String) {
        _noteContentInput.value = content
    }

    fun deleteNote() {
        viewModelScope.launch {
            try {
                noteId?.let {
                    deleteNoteUseCase.invoke(it)
                    _uiState.value =
                        _uiState.value.copy(message = R.string.note_deleted, navigateBack = true)
                }
            } catch (e: Exception) {
                setError(e)
            }
        }
    }

    fun consumeNavigateBack() {
        _uiState.value =
            _uiState.value.copy(navigateBack = false)
    }

    fun consumeMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    private fun setError(e: Throwable?) {
        _uiState.value = _uiState.value.copy(error = e)
    }
}