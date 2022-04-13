package com.entelgy.marvel.app.utils.base

interface Presenter<T : BaseView> {

    var view: T?

    fun create()
    fun resume()
    fun pause()
    fun stop()
    fun destroy()
}