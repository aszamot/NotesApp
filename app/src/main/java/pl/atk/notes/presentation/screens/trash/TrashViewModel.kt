package pl.atk.notes.presentation.screens.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.atk.notes.R
import pl.atk.notes.domain.usecases.delete.DeleteAllNotesInTrashUseCase
import pl.atk.notes.domain.usecases.delete.DeleteNoteUseCase
import pl.atk.notes.domain.usecases.delete.UnTrashNoteUseCase
import pl.atk.notes.domain.usecases.getnotes.GetTrashNotesUseCase
import pl.atk.notes.presentation.model.NoteItemUi
import pl.atk.notes.utils.extensions.toNoteItemUi
import javax.inject.Inject

@HiltViewModel
class TrashViewModel @Inject constructor(
    private val getTrashNotesUseCase: GetTrashNotesUseCase,
    private val unTrashNoteUseCase: UnTrashNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val deleteAllNotesInTrashUseCase: DeleteAllNotesInTrashUseCase
) : ViewModel() {

    private val _notesSelected = MutableStateFlow<MutableSet<NoteItemUi>>(mutableSetOf())

    private val _uiState = MutableStateFlow(TrashNotesListUiState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        TrashNotesListUiState()
    )

    init {
        observeTrashedNotesFlow()
        observeSelectedNotes()
    }

    fun observeTrashedNotesFlow() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        getTrashNotesUseCase.invoke()
            .map { notes ->
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        notes = notes.map { it.toNoteItemUi() },
                        notesEmpty = notes.isEmpty()
                    )
            }.catch { e ->
                setError(e)
            }.launchIn(viewModelScope)
    }

    private fun observeSelectedNotes() {
        _notesSelected.onEach { selectedNotes ->
            _uiState.value = _uiState.value.copy(
                selectedNotesCount = selectedNotes.size,
                isInNoteSelectedMode = selectedNotes.isNotEmpty(),
                notes = _uiState.value.notes.map { it.copy(isSelected = selectedNotes.contains(it)) }
            )
        }.launchIn(viewModelScope)
    }

    private fun setError(e: Throwable?) {
        _uiState.value = _uiState.value.copy(error = e)
    }

    fun selectNoteToggle(note: NoteItemUi) {
        val newSet = _notesSelected.value.toMutableSet()
        if (newSet.contains(note)) {
            newSet.remove(note)
        } else {
            newSet.add(note)
        }
        _notesSelected.value = newSet
    }

    fun removeAllSelectedNote() {
        _notesSelected.value = mutableSetOf()
    }

    fun isInNoteSelectedMode() = _uiState.value.isInNoteSelectedMode

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            try {
                _notesSelected.value.forEach { note ->
                    deleteNoteUseCase.invoke(noteId = note.id)
                }
                _uiState.value = _uiState.value.copy(message = R.string.notes_deleted)
                removeAllSelectedNote()
            } catch (e: Exception) {
                setError(e)
            }
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            deleteAllNotesInTrashUseCase.invoke()
        }
    }

    fun unTrashSelectedNotes() {
        viewModelScope.launch {
            try {
                _notesSelected.value.forEach { note ->
                    unTrashNoteUseCase.invoke(noteId = note.id)
                }
                _uiState.value = _uiState.value.copy(message = R.string.notes_un_trashed)
                removeAllSelectedNote()
            } catch (e: Exception) {
                setError(e)
            }
        }
    }

    fun consumeMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    fun consumeError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}