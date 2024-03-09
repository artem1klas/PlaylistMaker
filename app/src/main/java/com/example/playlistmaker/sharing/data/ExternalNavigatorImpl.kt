package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink() {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.https_yandex_ad))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (resolveActivity(context.packageManager) != null) {
                context.startActivity(this)
            }
        }
    }

    override fun openLink() {
        Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.https_offer))).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (resolveActivity(context.packageManager) != null) {
                context.startActivity(this)
            }
        }
    }

    override fun openEmail() {
        Intent(Intent.ACTION_SEND).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayListOf(context.getString(R.string.email_my)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.message_subject))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.message_text))
            if (resolveActivity(context.packageManager) != null) {
                context.startActivity(this)
            }
        }
    }
}