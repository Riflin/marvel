package com.entelgy.marvel.app.comicdetails.dialog.presenter

import android.os.Bundle
import com.entelgy.marvel.app.callbacks.ComicsCallback
import com.entelgy.marvel.app.comicdetails.dialog.ComicVariantsView
import com.entelgy.marvel.app.utils.base.Presenter

interface ComicVariantsPresenter: Presenter<ComicVariantsView>, ComicsCallback {

    fun getData(arguments: Bundle?)
}