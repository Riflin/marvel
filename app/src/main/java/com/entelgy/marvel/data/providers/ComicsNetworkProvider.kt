package com.entelgy.marvel.data.providers

import com.entelgy.marvel.data.model.comics.ComicDataWrapper
import com.entelgy.marvel.data.services.ComicsService
import retrofit2.Response

class ComicsNetworkProvider(private val api: ComicsService) {

    suspend fun getComics(): Response<ComicDataWrapper> {
        return api.getComics()
    }

    suspend fun getComicsFromCharacter(characeerId: Int): Response<ComicDataWrapper> {
        return api.getComicsFromCharacter(characeerId)
    }

    suspend fun getComicDetails(comicId: Int): Response<ComicDataWrapper> {
        return api.getComicDetails(comicId)
    }
}