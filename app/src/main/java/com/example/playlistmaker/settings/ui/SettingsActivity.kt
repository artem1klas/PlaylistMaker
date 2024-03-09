package com.example.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    private lateinit var binding: ActivitySettingsBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]

        binding.buttonArrowBack.setOnClickListener { finish() }

        viewModel.observeOnState().observe(this) {
            binding.themeSwitcher.isChecked = it
            AppCompatDelegate.setDefaultNightMode(
                if (it) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
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

}