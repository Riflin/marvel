package com.entelgy.marvel.data.providers

import com.entelgy.marvel.data.model.comics.ComicDataWrapper
import com.entelgy.marvel.data.services.ComicsService

class ComicsNetworkProvider(private val api: ComicsService) {

    suspend fun getComics(): ComicDataWrapper {
        return api.getComics()
    }

    suspend fun getComicsFromCharacter(characeerId: Int): ComicDataWrapper {
        return api.getComicsFromCharacter(characeerId)
    }

    suspend fun getComicDetails(comicId: Int): ComicDataWrapper {
        return api.getComicDetails(comicId)
    }
}