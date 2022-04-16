package com.entelgy.marvel.app.webview.presenter

import android.content.Intent
import com.entelgy.marvel.app.utils.base.Presenter
import com.entelgy.marvel.app.webview.WebView

interface WebPresenter: Presenter<WebView> {

    fun getData(intent: Intent?)

}