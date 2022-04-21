package com.entelgy.marvel.app.utils

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.entelgy.marvel.app.utils.views.DialogInformacion
import com.entelgy.marvel.data.services.ServiceFactory
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient

object AppUtils {

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

    /**
     * Método creado para calcular el número total de filas a mostrar en un adapter de los artículos
     * (comics, eventos, etc) de un personaje en particular
     */
    fun getItemCount(list: List<*>, totalItems: Int): Int {
        return when  {
            list.isEmpty() -> 0
            totalItems > 0 -> list.size + 1
            else -> list.size
        }
    }
}