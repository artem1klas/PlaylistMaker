package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

const val APP_THEME_SHARED_PREFENCES = "dark_theme_preferences"
const val IS_DARK_THEME_KEY = "key_for_dark_theme"

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPrefs = getSharedPreferences(APP_THEME_SHARED_PREFENCES, MODE_PRIVATE)

        val buttonArrowBack = findViewById<Button>(R.id.settings_arrow_back)
        val themeSwitcher = findViewById<SwitchCompat>(R.id.themeSwitcher)
        val buttonShare = findViewById<Button>(R.id.settings_share)
        val buttonSupport = findViewById<Button>(R.id.settings_support)
        val buttonAgreement = findViewById<Button>(R.id.settings_agreement)

        themeSwitcher.isChecked = (applicationContext as App).darkTheme



        buttonArrowBack.setOnClickListener { finish() }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)

            sharedPrefs.edit()
                .putBoolean(IS_DARK_THEME_KEY, checked)
                .apply()
        }

        buttonShare.setOnClickListener {
            val shareSend = Intent(Intent.ACTION_SEND)
            shareSend.type = "text/plan"
            shareSend.putExtra(Intent.EXTRA_TEXT, getString(R.string.https_yandex_ad))
            startActivity(shareSend)
        }

        buttonSupport.setOnClickListener {
            val supportSend = Intent(Intent.ACTION_SEND)
            supportSend.data = Uri.parse("mailto:")
            supportSend.putExtra(Intent.EXTRA_EMAIL, arrayListOf(getString(R.string.email_my)))
            supportSend.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.message_subject))
            supportSend.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_text))
            startActivity(supportSend)
        }

        buttonAgreement.setOnClickListener {
            val openUserAgreement =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.https_offer)))
            startActivity(openUserAgreement)
        }

    }

}