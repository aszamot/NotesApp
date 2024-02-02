package pl.atk.notes.presentation.screens.trash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pl.atk.notes.R
import pl.atk.notes.databinding.FragmentTrashBinding
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.presentation.model.NoteItemUi
import pl.atk.notes.presentation.screens.base.BaseFragment
import pl.atk.notes.presentation.screens.menu.CurrentMenuItem
import pl.atk.notes.presentation.utils.adapters.NotesAdapter
import pl.atk.notes.utils.extensions.empty

@AndroidEntryPoint
class TrashFragment : BaseFragment<FragmentTrashBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTrashBinding =
        FragmentTrashBinding::inflate

    private val viewModel: TrashViewModel by viewModels()

    private var notesAdapter: NotesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeUiState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        notesAdapter = null
    }

    private fun setupViews() {
        setupToolbar()
        setupSwipeRefreshLayout()
        setupNotesRecyclerView()
    }

    private fun setupToolbar() {
        with(binding) {
            trashToolbar.setNavigationOnClickListener { showMenuDialog() }
            trashToolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_delete_forever -> {
                        viewModel.deleteAllNotes()
                        true
                    }

                    else -> false
                }
            }
            trashActionToolbar.setNavigationOnClickListener { viewModel.removeAllSelectedNote() }
            trashActionToolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_un_trash -> {
                        viewModel.unTrashSelectedNotes()
                        true
                    }

                    R.id.menu_delete_forever -> {
                        viewModel.deleteSelectedNotes()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun showMenuDialog() {
        val action = TrashFragmentDirections.actionTrashFragmentToMenuDialog(
            currentMenuItem = CurrentMenuItem.TRASH
        )
        findNavController().navigate(action)
    }

    private fun setupSwipeRefreshLayout() {
        with(binding.swipeToRefresh) {
            setOnRefreshListener { viewModel.observeTrashedNotesFlow() }
        }
    }

    private fun setupNotesRecyclerView() {
        binding.recyclerNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            notesAdapter = NotesAdapter(
                onNoteClickListener = ::onNoteClick,
                onNoteLongClickListener = ::onNoteLongClick
            )
            adapter = notesAdapter
        }
    }

    private fun onNoteClick(note: NoteItemUi) {
        if (viewModel.isInNoteSelectedMode()) {
            viewModel.selectNoteToggle(note)
        } else {
            showSnackbar("GO TO EDIT NOTE")
        }
    }

    private fun onNoteLongClick(note: NoteItemUi) {
        viewModel.selectNoteToggle(note)
    }

    private fun observeUiState() {
        viewModel.uiState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { uiState ->
                renderUiState(uiState)
            }
            .launchIn(lifecycleScope)
    }

    private fun renderUiState(uiState: TrashNotesListUiState) {
        with(binding) {
            swipeToRefresh.isRefreshing = uiState.isLoading

            notesAdapter?.submitList(uiState.notes)

            setToolbarSelectedNotesCount(uiState.selectedNotesCount)
            setNoteSelectedMode(uiState.isInNoteSelectedMode)

            if (uiState.message != null) {
                showSnackbar(uiState.message)
                viewModel.consumeMessage()
            }

            if (uiState.error != null) {
                when (uiState.error) {
                    is NoteNotFoundException -> showSnackbar(R.string.error_note_not_found)
                    else -> showSnackbar(R.string.error)
                }
                viewModel.consumeError()
            }
        }
    }

    private fun setToolbarSelectedNotesCount(count: Int) {
        binding.trashActionToolbar.title = if (count > 0)
            count.toString()
        else
            String.empty
    }

    private fun setNoteSelectedMode(isNoteSelected: Boolean) {
        with(binding) {
            trashToolbar.isVisible = !isNoteSelected
            trashActionToolbar.isVisible = isNoteSelected
        }
    }
}