package com.entelgy.marvel.app.photolist.presenter

import android.content.Intent
import com.entelgy.marvel.app.photolist.PhotoListView
import com.entelgy.marvel.app.utils.base.Presenter

interface PhotoListPresenter: Presenter<PhotoListView> {

    fun getData(intent: Intent?)
}