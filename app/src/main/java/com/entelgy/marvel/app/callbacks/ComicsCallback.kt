package com.entelgy.marvel.app.callbacks

import com.entelgy.marvel.data.model.ComicSummary

interface ComicsCallback {

    fun onComicSelected(comic: ComicSummary)

    fun onMoreComicsSelected()
}