package com.entelgy.marvel.app.photolist.presenter

import android.content.Intent
import com.entelgy.marvel.app.photolist.PhotoListView
import com.entelgy.marvel.data.model.Image
import com.entelgy.marvel.data.utils.Constants

class PhotoListPresenterImpl : PhotoListPresenter {
    override var view: PhotoListView? = null

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

    override fun getData(intent: Intent?) {
        //Comprobamos que no hay errores
        if (intent != null) {
            val images: ArrayList<Image>? = intent.getParcelableArrayListExtra(Constants.IMAGES)
            //Si no tenemos imágenes, error al canto
            if (images.isNullOrEmpty()) {
                view?.onDataError()
            } else {
                //Si no, las mostramos y ya
                view?.showImages(images)
            }
        } else {
            view?.onDataError()
        }
    }
}