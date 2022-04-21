package com.entelgy.marvel.app.callbacks

/**
 * Callback para el diálogo donde se seleccione un nombre para filtrar. Deben implementarlo los presenters
 * que lo necesiten
 */
interface NameFilterCallback {

    fun onNameFilterSelected(name: String?)
}