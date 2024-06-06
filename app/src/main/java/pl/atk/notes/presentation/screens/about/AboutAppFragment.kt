package pl.atk.notes.presentation.screens.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.atk.notes.BuildConfig
import pl.atk.notes.R
import pl.atk.notes.databinding.FragmentAboutAppBinding
import pl.atk.notes.presentation.screens.base.BaseFragment
import pl.atk.notes.utils.extensions.isInNightMode

@AndroidEntryPoint
class AboutAppFragment : BaseFragment<FragmentAboutAppBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAboutAppBinding
        get() = FragmentAboutAppBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupToolbar()
        setupVersion()
        setupRepoCard()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupVersion() {
        binding.tvAppVersion.text = BuildConfig.VERSION_NAME
    }

    private fun setupRepoCard() {
        with(binding) {
            ivGithubLogo.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (requireContext().isInNightMode()) R.drawable.ic_github_white else R.drawable.ic_github
                )
            )
            cardRepo.setOnClickListener { openLink(getString(R.string.about_app_repo_link)) }
        }
    }
}