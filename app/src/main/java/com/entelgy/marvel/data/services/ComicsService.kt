package com.entelgy.marvel.data.services

import com.entelgy.marvel.data.model.comics.ComicDataWrapper
import retrofit2.http.GET
import retrofit2.http.Path

interface ComicsService {

    @GET("v1/public/comics")
    suspend fun getComics(): ComicDataWrapper

    @GET("/v1/public/characters/{characterId}/comics")
    suspend fun getComicsFromCharacter(@Path("characterId") characterId: Int): ComicDataWrapper

    @GET("/v1/public/comics/{comicId}")
    suspend fun getComicDetails(@Path("comicId") characterId: Int): ComicDataWrapper
}