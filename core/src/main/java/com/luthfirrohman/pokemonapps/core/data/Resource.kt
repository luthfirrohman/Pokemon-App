package com.luthfirrohman.movieapps.core.data

import com.luthfirrohman.movieapps.core.data.Status.SUCCESS
import com.luthfirrohman.movieapps.core.data.Status.ERROR
import com.luthfirrohman.movieapps.core.data.Status.LOADING

sealed class Resource<T>(val status: Status, val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(SUCCESS, data)
    class Loading<T>(data: T? = null) : Resource<T>(LOADING, data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(ERROR, data, message)
}