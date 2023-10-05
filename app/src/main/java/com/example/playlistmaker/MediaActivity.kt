package com.example.playlistmaker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MediaActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val buttonArrowBack = findViewById<Button>(R.id.media_arrow_back)
        buttonArrowBack.setOnClickListener { finish() }
    }
}