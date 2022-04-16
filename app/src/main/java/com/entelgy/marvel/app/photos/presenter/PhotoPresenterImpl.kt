package com.entelgy.marvel.app.photos.presenter

import android.content.Intent
import com.entelgy.marvel.app.photos.PhotoView
import com.entelgy.marvel.data.utils.Constants

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

    override fun getData(intent: Intent?) {
        if (intent != null) {
            val url = intent.getStringExtra(Constants.URL)
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