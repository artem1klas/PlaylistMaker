package com.example.playlistmaker.utils

import android.content.Context
import android.util.TypedValue
import com.example.playlistmaker.R

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

fun declineTrack(context: Context, num: Int): String {
    if(num % 100 / 10 == 1){
        return context.getString(R.string.decline_track_11)
    }
    return when(num % 10){
        1 -> context.getString(R.string.decline_track_1)
        in 2..4 -> context.getString(R.string.decline_track_2_4)
        else -> context.getString(R.string.decline_track_default)
    }
}

