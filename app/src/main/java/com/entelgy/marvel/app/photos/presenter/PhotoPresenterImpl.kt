package com.entelgy.marvel.app.photos.presenter

import android.content.Intent
import com.entelgy.marvel.app.photos.PhotoView
import com.entelgy.marvel.data.utils.Constants

/**
 * Presenter para la pantalla donde mostramos una imagen en particular (la imagen de un cómic o personaje)
 */
class PhotoPresenterImpl : PhotoPresenter {
    override var view: PhotoView? = null

    override fun create() {

    }

    override fun resume() {

    }

    override fun pause() {

    }

    override fun stop() {

    }

    override fun destroy() {
        view = null
    }

    /**
     * Comprobamos que no hay errores
     */
    override fun getData(intent: Intent?) {
        if (intent != null) {
            //Tenemos que tener una url desde la que cargar la foto
            val url = intent.getStringExtra(Constants.URL)
            //Si tenemos, la mostramos
            if (url != null) {
                view?.showPhoto(url)
            } else {
                view?.onDataError()
            }
        } else {
            view?.onDataError()
        }
    }
}