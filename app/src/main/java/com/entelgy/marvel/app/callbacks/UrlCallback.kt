package com.entelgy.marvel.app.callbacks

import com.entelgy.marvel.data.model.Url

/**
 * Callback para los adapters donde se muestran los enlaces. Deben implementarlo los presenters
 * que lo necesiten
 */
interface UrlCallback {

    fun onUrlSelected(url: Url)
}