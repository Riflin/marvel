package com.entelgy.marvel.domain

import retrofit2.Response

abstract class NetworkUseCase<T> {

    abstract suspend fun downloadData(): Response<T>
}