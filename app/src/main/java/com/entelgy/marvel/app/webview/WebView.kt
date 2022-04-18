package com.entelgy.marvel.app.webview

import com.entelgy.marvel.app.utils.base.BaseView

interface WebView: BaseView {

    fun showWebpage(url: String)
    fun onUrlInvalid()
}