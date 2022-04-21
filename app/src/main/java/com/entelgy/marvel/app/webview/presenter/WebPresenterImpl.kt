package com.entelgy.marvel.app.webview.presenter

import android.content.Intent
import com.entelgy.marvel.app.webview.WebView
import com.entelgy.marvel.data.model.Url
import com.entelgy.marvel.data.utils.Constants

/**
 * Presenter para el webView que muestra el navegador con el que abrimos los enlaces de la app
 */
class WebPresenterImpl : WebPresenter {
    override var view: WebView? = null

    private var url: Url? = null

    override fun create() {
        url = null
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
     * Comprobamos que realmente tenemos una url a la que navegar
     */
    override fun getData(intent: Intent?) {
        if (intent != null) {
            val url: Url? = intent.getParcelableExtra(Constants.URL)
            if (url != null) {
                if (url.url != null) {
                    view?.showWebpage(url.url)
                } else {
                    view?.onUrlInvalid()
                }
            } else {
                view?.onDataError()
            }
        } else {
            view?.onDataError()
        }
    }
}