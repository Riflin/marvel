package com.entelgy.marvel.app.comicdetails.presenter

import android.content.Intent
import com.entelgy.marvel.app.callbacks.*
import com.entelgy.marvel.app.comicdetails.ComicDetailsView
import com.entelgy.marvel.app.utils.base.Presenter

interface ComicDetailsPresenter: Presenter<ComicDetailsView>, CharactersCallback, SeriesCallback,
    EventCallback, StoriesCallback, UrlCallback {

    fun getData(intent: Intent?)

    fun showComics()

    fun showSeries()

    fun showStories()

    fun showEvents()

    fun showPhotoDetail()

    fun showVariants()

    fun showImages()
}