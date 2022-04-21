package com.entelgy.marvel.app.utils.base

//Interfaz madre para los presenter de la aplicación
interface Presenter<T : BaseView> {

    var view: T?

    //Métodos a llamar con el ciclo de vida de la aplicación
    fun create()
    fun resume()
    fun pause()
    fun stop()
    fun destroy()
}