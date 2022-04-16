package com.entelgy.marvel.app.callbacks

import com.entelgy.marvel.data.model.Url

interface UrlCallback {

    fun onUrlSelected(url: Url)
}