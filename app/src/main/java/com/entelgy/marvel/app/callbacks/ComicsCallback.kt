package com.entelgy.marvel.app.callbacks

import com.entelgy.marvel.data.model.characters.ComicSummary

/**
 * Callback para los adapters donde se muestran los cómics. Deben implementarlo los presenters
 * que lo necesiten
 */
interface ComicsCallback {

    fun onComicSelected(comic: ComicSummary)

    fun onMoreComicsSelected()
}