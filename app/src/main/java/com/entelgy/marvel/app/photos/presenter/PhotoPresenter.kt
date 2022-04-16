package com.entelgy.marvel.app.photos.presenter

import android.content.Intent
import com.entelgy.marvel.app.photos.PhotoView
import com.entelgy.marvel.app.utils.base.Presenter

interface PhotoPresenter: Presenter<PhotoView> {

    fun getData(intent: Intent?)
}