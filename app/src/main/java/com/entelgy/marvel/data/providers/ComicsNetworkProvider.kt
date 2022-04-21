package com.entelgy.marvel.data.providers

import com.entelgy.marvel.data.model.comics.ComicDataWrapper
import com.entelgy.marvel.data.services.ComicsService
import retrofit2.Response

/**
 * Provider para obtener los datos de los comics desde el servidor
 */
class ComicsNetworkProvider(private val api: ComicsService) {

    /**
     * Información de los comics
     */
    suspend fun getComics(): Response<ComicDataWrapper> {
        return api.getComics()
    }

    /**
     * Comics en los que aparece un personaje en concreto
     */
    suspend fun getComicsFromCharacter(characterId: Int): Response<ComicDataWrapper> {
        return api.getComicsFromCharacter(characterId)
    }

    /**
     * Detalles de un cómic en particular
     */
    suspend fun getComicDetails(comicId: Int): Response<ComicDataWrapper> {
        return api.getComicDetails(comicId)
    }
}