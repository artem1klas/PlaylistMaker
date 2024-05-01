package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeOnState().observe(viewLifecycleOwner) {
            binding.themeSwitcher.isChecked = it
            App.setDarkTheme(it)
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.onSwitchClick(checked)
        }

        binding.buttonShare.setOnClickListener {
            viewModel.shareApp()
        }

        binding.buttonSupport.setOnClickListener {
            viewModel.openSupport()
        }

        binding.buttonAgreement.setOnClickListener {
            viewModel.openTerms()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}