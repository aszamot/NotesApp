package pl.atk.notes.presentation.screens.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import pl.atk.notes.R
import pl.atk.notes.databinding.DialogMenuBinding
import pl.atk.notes.presentation.screens.base.BaseBottomSheetDialogFragment

class MenuDialog : BaseBottomSheetDialogFragment<DialogMenuBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> DialogMenuBinding
        get() = DialogMenuBinding::inflate

    private val args: MenuDialogArgs by navArgs()
    private val currentMenuItem by lazy {
        args.currentMenuItem
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setCurrentMenuItem() {
        val background =
            ContextCompat.getDrawable(requireContext(), R.drawable.ripple_selected_menu_item)
        with(binding) {
            when (currentMenuItem) {
                CurrentMenuItem.NOTES -> tvNotes.background = background
                CurrentMenuItem.ARCHIVE -> tvArchive.background = background
                CurrentMenuItem.TRASH -> tvTrash.background = background
            }
        }
    }

    private fun setupViews() {
        setCurrentMenuItem()
        with(binding) {
            tvNotes.setOnClickListener { navigateToNotesScreen() }
            tvArchive.setOnClickListener { navigateToArchiveScreen() }
            tvTrash.setOnClickListener { navigateToTrashScreen() }
        }
    }

    private fun navigateToNotesScreen() {
        findNavController().popBackStack(R.id.notesListFragment, false)
    }

    private fun navigateToArchiveScreen() {
        if (currentMenuItem == CurrentMenuItem.ARCHIVE)
            dismiss()
        else {
            findNavController().navigate(MenuDialogDirections.actionMenuDialogToArchiveFragment())
        }
    }

    private fun navigateToTrashScreen() {
        if (currentMenuItem == CurrentMenuItem.TRASH)
            dismiss()
        else {
            findNavController().navigate(MenuDialogDirections.actionMenuDialogToTrashFragment())
        }
    }
}