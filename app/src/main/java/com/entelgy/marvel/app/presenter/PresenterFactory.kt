package com.entelgy.marvel.app.presenter

import com.entelgy.marvel.app.characterslist.CharactersListView
import com.entelgy.marvel.app.characterslist.presenter.CharactersListPresenter
import com.entelgy.marvel.app.characterslist.presenter.CharactersListPresenterImpl

object PresenterFactory {

    private var charactersListPresenter: CharactersListPresenter? = null

    fun getCharactersListPresenter(): CharactersListPresenter {
        return if (charactersListPresenter == null) {
            charactersListPresenter = CharactersListPresenterImpl()
            charactersListPresenter!!
        } else {
            charactersListPresenter!!
        }
    }
}