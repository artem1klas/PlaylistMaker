package com.example.playlistmaker.search.ui3

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType


interface SSearchView: MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: SState)
}