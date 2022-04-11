package com.entelgy.marvel.app.utils

import android.view.View
import androidx.fragment.app.FragmentManager
import com.entelgy.marvel.app.utils.views.DialogInformacion

object Utils {

    fun showDialogInformacion(
        fragmentManager: FragmentManager,
        title: String?,
        text: String?,
        onClickListener: View.OnClickListener?
    ) {
        val dialogInformacion: DialogInformacion =
            DialogInformacion.createNewInstance(title, text, onClickListener)
        dialogInformacion.show(fragmentManager, "Info")
    }

    fun showDialogInformacion(
        fragmentManager: FragmentManager,
        title: String?,
        text: String?,
        onClickListener: () -> Unit
    ) {
        val dialogInformacion: DialogInformacion =
            DialogInformacion.createNewInstance(title, text, onClickListener)
        dialogInformacion.show(fragmentManager, "Info")
    }

    fun showDialogInformacion(fragmentManager: FragmentManager, title: String?, text: String?) {
        showDialogInformacion(fragmentManager, title, text, null)
    }
}