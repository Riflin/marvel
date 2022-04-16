package com.entelgy.marvel.app.photos

import com.entelgy.marvel.app.utils.base.BaseView

interface PhotoView: BaseView {

    fun showPhoto(url: String)

    fun onDataError()
}