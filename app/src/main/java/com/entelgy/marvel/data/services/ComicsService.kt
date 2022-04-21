package com.entelgy.marvel.data.services

import com.entelgy.marvel.data.model.comics.ComicDataWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * LLamadas para obtener información de los comics y sus parámetros
 */
interface ComicsService {

    @GET("v1/public/comics")
    suspend fun getComics(): Response<ComicDataWrapper>

    @GET("/v1/public/characters/{characterId}/comics")
    suspend fun getComicsFromCharacter(@Path("characterId") characterId: Int): Response<ComicDataWrapper>

    @GET("/v1/public/comics/{comicId}")
    suspend fun getComicDetails(@Path("comicId") characterId: Int): Response<ComicDataWrapper>
}