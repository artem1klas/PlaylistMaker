package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.search.ui2.SearchPresenter

class App : Application() {

    var searchPresenter : SearchPresenter? = null

     internal var darkTheme = false


}