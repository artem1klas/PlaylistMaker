package com.example.playlistmaker

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.main_search)
        val buttonMedia = findViewById<Button>(R.id.main_media)
        val buttonSettings = findViewById<Button>(R.id.main_settings)

        val searchIntent = Intent(this, SearchActivity::class.java)
        val mediaIntent = Intent(this, MediaActivity::class.java)
        val settingsIntent = Intent(this, SettingsActivity::class.java)

        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(searchIntent)
            }
        }
        buttonSearch.setOnClickListener(searchClickListener)

        buttonMedia.setOnClickListener { startActivity(mediaIntent) }

        buttonSettings.setOnClickListener { startActivity(settingsIntent) }

    }
}