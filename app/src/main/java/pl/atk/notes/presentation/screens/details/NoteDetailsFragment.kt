package pl.atk.notes.presentation.screens.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pl.atk.notes.R
import pl.atk.notes.databinding.FragmentNoteDetailsBinding
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.presentation.screens.base.BaseFragment
import pl.atk.notes.presentation.screens.confirmation.ConfirmationDialog
import pl.atk.notes.utils.extensions.toReadableString

@AndroidEntryPoint
class NoteDetailsFragment : BaseFragment<FragmentNoteDetailsBinding>() {

    private val viewModel: NoteDetailsViewModel by viewModels()

    private var isUpdatingProgrammatically = false
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNoteDetailsBinding
        get() = FragmentNoteDetailsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeUiState()
    }

    private fun setupViews() {
        setupToolbar()
        setupEditTexts()
    }

    private fun setupToolbar() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            toolbar.inflateMenu(R.menu.menu_delete)
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_delete -> showDeleteConfirmationDialog()
                }
                false
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val dialog =
            ConfirmationDialog(
                confirmationQuestionId = R.string.note_delete_confirmation_question,
                onConfirm = {
                    viewModel.deleteNote()
                })
        dialog.show(childFragmentManager, ConfirmationDialog.TAG)
    }

    private fun setupEditTexts() {
        with(binding) {
            tieTitle.doOnTextChanged { text, _, _, _ ->
                if (!isUpdatingProgrammatically) {
                    viewModel.updateTitle(text.toString())
                }
            }
            tieContent.doOnTextChanged { text, _, _, _ ->
                if (!isUpdatingProgrammatically) {
                    viewModel.updateContent(text.toString())
                }
            }
        }
    }

    private fun observeUiState() {
        viewModel.uiState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { uiState ->
                renderUiState(uiState)
            }
            .launchIn(lifecycleScope)
    }

    private fun renderUiState(uiState: NoteDetailsUiState) {
        with(binding) {
            isUpdatingProgrammatically = true

            if (tieTitle.text.toString() != uiState.noteTitle) {
                tieTitle.setText(uiState.noteTitle)
            }
            if (tieContent.text.toString() != uiState.noteContent) {
                tieContent.setText(uiState.noteContent)
            }

            isUpdatingProgrammatically = false

            uiState.lastChangedDate?.let {
                toolbar.title = getString(R.string.note_last_changed, it.toReadableString())
            }

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

            if (uiState.navigateBack) {
                viewModel.consumeNavigateBack()
                findNavController().popBackStack()
            }

        }
    }
}