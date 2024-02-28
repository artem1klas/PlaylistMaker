package com.example.playlistmaker.main.ui

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.playlistmaker.media.MediaActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.search.ui2.Search
import com.example.playlistmaker.search.ui3.SSearch
import com.example.playlistmaker.search.ui4.SSSearch
import com.example.playlistmaker.search.ui5.NewSearch
import com.example.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.main_search)
        val buttonMedia = findViewById<Button>(R.id.main_media)
        val buttonSettings = findViewById<Button>(R.id.main_settings)

//        val searchIntent = Intent(this, SearchActivity::class.java)
        val searchIntent = Intent(this, NewSearch::class.java)
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