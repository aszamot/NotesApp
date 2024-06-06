package pl.atk.notes.presentation.screens.confirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import pl.atk.notes.databinding.DialogConfirmationBinding
import pl.atk.notes.presentation.screens.base.BaseBottomSheetDialogFragment

class ConfirmationDialog(
    @StringRes private val confirmationQuestionId: Int,
    private val onConfirm: () -> Unit,
) : BaseBottomSheetDialogFragment<DialogConfirmationBinding>() {

    companion object {
        const val TAG = "ConfirmationDialog"
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> DialogConfirmationBinding
        get() = DialogConfirmationBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupQuestion()
        setupButtons()
    }

    private fun setupQuestion() {
        binding.tvConfirmationQuestion.text = getString(confirmationQuestionId)
    }

    private fun setupButtons() {
        with(binding) {
            btnYes.setOnClickListener {
                onConfirm.invoke()
                dismiss()
            }
            btnNo.setOnClickListener { dismiss() }
        }
    }
}