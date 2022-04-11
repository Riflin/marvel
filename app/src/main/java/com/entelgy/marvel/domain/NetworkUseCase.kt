package com.entelgy.marvel.domain

abstract class NetworkUseCase<T> {

    abstract suspend fun downloadData(): T
}