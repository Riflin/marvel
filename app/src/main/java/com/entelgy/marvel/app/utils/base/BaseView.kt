package com.entelgy.marvel.app.utils.base

import android.content.Context
import androidx.fragment.app.FragmentManager

interface BaseView {
    fun showLoading(show: Boolean = true)

    /**
     * Se llama viewContext y no context para evitar el fallo de "same signature" con el getContext()
     * de los fragments
     */
    val viewContext: Context

    fun showError(message: String)

    /**
     * Este método lo metemos en la vista para poder crear diálogos directamente desde el presenter
     */
    fun getSupportFragmentManager(): FragmentManager

    /**
     * Método en el que cada actividad/fragment indicará que no se ha abierto con los datos necesarios
     * y, normalmente, se cerrará impidiendo un uso incorrecto de la misma
     */
    fun onDataError()
}