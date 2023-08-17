package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonArrowBack = findViewById<Button>(R.id.settings_arrow_back)
        val buttonShare = findViewById<Button>(R.id.settings_share)
        val buttonSupport = findViewById<Button>(R.id.settings_support)
        val buttonAgreement = findViewById<Button>(R.id.settings_agreement)

        buttonArrowBack.setOnClickListener{ finish() }

        buttonShare.setOnClickListener {
            val shareSend = Intent(Intent.ACTION_SEND)
            shareSend.type = "text/plan"
            shareSend.putExtra(Intent.EXTRA_TEXT, getString(R.string.https_yandex_ad))
            startActivity(shareSend)
        }

        //перепроверить отправку сообщений

        buttonSupport.setOnClickListener {
            val supportSend = Intent(Intent.ACTION_SEND)
            supportSend.data = Uri.parse("mailto:")
            supportSend.putExtra(Intent.EXTRA_EMAIL, arrayListOf(getString(R.string.email_my)))
            supportSend.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.message_subject))
            supportSend.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_text))
            startActivity(supportSend)
        }

        buttonAgreement.setOnClickListener {
            val openUserAgreement = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.https_offer)))
            startActivity(openUserAgreement)
        }

    }

}