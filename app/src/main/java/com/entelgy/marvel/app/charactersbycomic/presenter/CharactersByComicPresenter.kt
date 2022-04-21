package com.entelgy.marvel.app.charactersbycomic.presenter

import android.content.Intent
import com.entelgy.marvel.app.callbacks.NameFilterCallback
import com.entelgy.marvel.app.charactersbycomic.CharactersByComicView
import com.entelgy.marvel.app.characterslist.presenter.CharactersListPresenter

interface CharactersByComicPresenter: CharactersListPresenter<CharactersByComicView>, NameFilterCallback {

    fun getData(intent: Intent?)
}