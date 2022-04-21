package com.entelgy.marvel.app.utils.base

import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.entelgy.marvel.R
import com.entelgy.marvel.app.utils.AppUtils

/**
 * Clase base para todas nuestras activities
 */
abstract class BaseActivity: AppCompatActivity(), BaseView {

    override val viewContext: Context
        get() = this

    /**
     * Cuando asignemos la vista a la pantalla, llamaremos directamente a los tres métodos
     * donde inicializaremos todo lo necesario
     */
    override fun setContentView(view: View?) {
        super.setContentView(view)

        init()

        initViews()

        attachListenersToTheViews()
    }

    /**
     * Por defecto, cerraremos la pantalla en la que estamos al darle a la flechita hacia atrás
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    /**
     * Aquí inicializaremos todo lo necesario para las activities. Normalmente lo que haremos será
     * inicializar el presenter
     */
    abstract fun init()

    /**
     * Método en el que inicializamos cualquier vista, ya sean recyclerViews, actionBar o lo que necesitemos
     */
    abstract fun initViews()

    /**
     * Método en el que asignamos los listeners a cada una de las vistas para indicarles qué deben hacer
     */
    abstract fun attachListenersToTheViews()

    override fun showError(message: String) {
        AppUtils.showDialogInformacion(supportFragmentManager, getString(R.string.error), message)
    }

    /**
     * Por defecto, mostramos un DialogInformación indicando que no hemos obtenido bien los datos
     * y cerraremos la actividad. Cada activity es libre de sobreescribir el método y hacer otra cosa, claro
     */
    override fun onDataError() {
        AppUtils.showDialogInformacion(supportFragmentManager, getString(R.string.error),
            getString(R.string.error_obteniendo_datos)) { finish() }
    }
}