package com.entelgy.marvel.app.characterdetails.presenter

import android.content.Intent
import com.entelgy.marvel.app.callbacks.*
import com.entelgy.marvel.app.characterdetails.CharacterDetailsView
import com.entelgy.marvel.app.utils.base.Presenter

interface CharacterDetailsPresenter: Presenter<CharacterDetailsView>, ComicsCallback, EventCallback,
    SeriesCallback, StoriesCallback, UrlCallback {

    fun getData(intent: Intent?)

    fun showComics()

    fun showSeries()

    fun showStories()

    fun showEvents()
    fun showPhotoDetail()
}