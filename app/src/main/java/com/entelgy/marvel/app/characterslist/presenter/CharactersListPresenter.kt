package com.entelgy.marvel.app.characterslist.presenter

import com.entelgy.marvel.app.callbacks.CharactersCallback
import com.entelgy.marvel.app.characterslist.CharactersListView
import com.entelgy.marvel.app.utils.base.Presenter

/**
 * Este presenter lo creamos parametrizado para que pueda ser heredado por el presenter
 * de los personajes por cómic (@see CharactersByComicPresenter) y así poder reutilizar código
 * y no tener que reescribir la misma funcionalidad dos veces
 */
interface CharactersListPresenter<T: CharactersListView>: Presenter<T>, CharactersCallback {

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