package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest

class RetrofitNetworkClient(
    private val context: Context,
    private val iTunesApiService: ITunesApiService
) : NetworkClient {
    override fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { -1 }
        }

        if (dto !is TrackSearchRequest) {
            return Response().apply { 400 }
        }
        val resp = iTunesApiService.search(dto.expression).execute()
        val body = resp.body()
        return if (body != null) {
            body.apply { resultCode = resp.code() }
        } else {
            Response().apply { resultCode = resp.code() }
        }
    }

    private fun isConnected(): Boolean {
        val connectiveManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectiveManager.getNetworkCapabilities(connectiveManager.activeNetwork)

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}