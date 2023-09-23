package com.example.playlistmaker

import TrackAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ITunesApi::class.java)

    private var textSearch = ""
    private val adapter = TrackAdapter()
    private lateinit var trackList: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val backButton = findViewById<Button>(R.id.back)
        val queryInput = findViewById<EditText>(R.id.queryInput)
        val clearButton = findViewById<Button>(R.id.clear)
        val windowDisconnect = findViewById<LinearLayout>(R.id.window_disconnect)
        val windowNotFound = findViewById<LinearLayout>(R.id.window_not_found)
        val windowTrackList = findViewById<RecyclerView>(R.id.window_trackList)
        val updateButton = findViewById<Button>(R.id.problem_updateButton)




        trackList = findViewById<RecyclerView>(R.id.window_trackList)
        trackList.layoutManager = LinearLayoutManager(this)
        trackList.adapter = adapter



        backButton.setOnClickListener {
            finish()
        }

        fun updateElementVisible(type: StatementType) {
            when (type) {
                StatementType.SUCCESS -> {
                    windowDisconnect.visibility = View.GONE
                    windowNotFound.visibility = View.GONE
                    windowTrackList.visibility = View.VISIBLE
                }

                StatementType.NOT_FOUND -> {
                    windowDisconnect.visibility = View.GONE
                    windowNotFound.visibility = View.VISIBLE
                    windowTrackList.visibility = View.GONE
                }

                StatementType.NO_CONNECTION -> {
                    windowDisconnect.visibility = View.VISIBLE
                    windowNotFound.visibility = View.GONE
                    windowTrackList.visibility = View.GONE

                }

            }
            adapter.notifyDataSetChanged()
        }

        clearButton.setOnClickListener {
            textSearch = ""
            queryInput.setText(textSearch)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            adapter.tracks.clear()
            updateElementVisible(StatementType.SUCCESS)
            adapter.notifyDataSetChanged()
        }

        fun search() {
            itunesService.search(queryInput.text.toString()).enqueue(object :
                Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        adapter.tracks.clear()

                        if (response.body()?.results?.isNotEmpty() == true) {
                            adapter.tracks.addAll(response.body()?.results!!)
                            updateElementVisible(StatementType.SUCCESS)
                        } else if (adapter.tracks.isEmpty()) {
                            adapter.tracks.clear()
                            updateElementVisible(StatementType.NOT_FOUND)
                        }
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    adapter.tracks.clear()
                    updateElementVisible(StatementType.NO_CONNECTION)
                }

            })
        }

        updateButton.setOnClickListener {
            search()
        }



        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }


        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(p0)
                textSearch = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }
        queryInput.addTextChangedListener(searchTextWatcher)


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_SEARCH, textSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(TEXT_SEARCH, "")
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
    }
}

enum class StatementType {
    NO_CONNECTION,
    NOT_FOUND,
    SUCCESS
}








