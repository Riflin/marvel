package com.entelgy.marvel.app.utils.base

interface Presenter<T : BaseView> {
    fun <E : T> setView(view: E)
    val view: T

    fun create()
    fun resume()
    fun pause()
    fun stop()
    fun destroy()
}