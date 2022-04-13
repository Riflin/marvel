package com.entelgy.marvel.app.utils.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.entelgy.marvel.R

/**
 * Clase madre para todos los dialogs de la aplicación. No se podrán cancelar tocando fuera de la pantalla (sí dándole al botón atrás) y heredarán del
 * estilo DialogStyle, que ya les fija el ancho a todos
 */
abstract class BaseDialogFragment: androidx.fragment.app.DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    /**
     * En el onCreate pondremos el estilo y llamaremos al método que inicializa todo lo necesario (normalmente variables que necesita el dialog para
     * ser útil) para el correcto funcionamiento del dialog
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)

        isCancelable = true

        init()
    }

    /**
     * Una vez se ha creado la vista, llamamos al método de inicialización de vistas (para inicializar recyclers, etc) y asignamos los listeners a cada una
     * de ellas. Lo hacemos aquí y no en el onCreateView para que kotlin y su manera de pillar las vistas sin necesidad de llamar al findViewById funcione
     * correctamente.
     * También llamamos al método showData, en el que haremos lo que necesitemos para mostrar los datos que queremos en el dialog
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        attachListenersToTheViews()

        showData()
    }

    abstract fun init()

    abstract fun initViews(view: View)

    abstract fun attachListenersToTheViews()

    abstract fun showData()
}