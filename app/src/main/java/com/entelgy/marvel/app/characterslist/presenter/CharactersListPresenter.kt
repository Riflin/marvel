package com.entelgy.marvel.app.characterslist.presenter

import com.entelgy.marvel.app.callbacks.CharactersCallback
import com.entelgy.marvel.app.characterslist.CharactersListView
import com.entelgy.marvel.app.utils.base.Presenter

interface CharactersListPresenter: Presenter<CharactersListView>, CharactersCallback {

    fun selectNameFilter()

    fun resetNameFilter()

    fun selectDateFilter()

    fun resetDateFilter()

    fun sortByName()

    fun resetSortByName()

    fun sortByDate()

    fun resetSortByDate()

    fun getDataFromServer()

    fun getMoreCharacters()
}