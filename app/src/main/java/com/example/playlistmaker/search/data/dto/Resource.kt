package com.example.playlistmaker.search.data.dto

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Succes<T>(data: T): Resource<T>(data = data)
    class Error<T>(message: String): Resource<T>(message = message)
}