package com.entelgy.marvel.app.charactersbycomic

import com.entelgy.marvel.app.characterslist.CharactersListView

/**
 * Esta pantalla es una "copia" de la del listado de personajes, únicamente con el añadido del nombre del comic
 * (y que las llamadas para obtener los personajes son a otro endpoint, claro), por lo que, en lugar de
 * heredar de BaseView, esta vista la hacemos hija de la CharactersListView
 */
interface CharactersByComicView: CharactersListView {

    fun showComicName(name: String)
}