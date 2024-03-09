package com.example.playlistmaker.search.domain.models

sealed class Resource<T>(val data: T? = null, val typeError: TypeError? = null) {
    class Succes<T>(data: T): Resource<T>(data = data)
    class Error<T>(typeError: TypeError): Resource<T>(typeError = typeError)
}