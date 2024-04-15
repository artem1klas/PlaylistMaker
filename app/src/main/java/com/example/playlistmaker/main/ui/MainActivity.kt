package com.example.playlistmaker.main.ui

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.media.MediaFragment
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.ui.SettingsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.main_search)
        val buttonMedia = findViewById<Button>(R.id.main_media)
        val buttonSettings = findViewById<Button>(R.id.main_settings)


        val mediaIntent = Intent(this, MediaFragment::class.java)
        val settingsIntent = Intent(this, SettingsFragment::class.java)



        buttonMedia.setOnClickListener { startActivity(mediaIntent) }

        buttonSettings.setOnClickListener { startActivity(settingsIntent) }
    }
}