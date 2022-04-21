package com.entelgy.marvel.data.services

import com.entelgy.marvel.data.model.characters.CharacterDataWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Definición de las llamadas al servidor concernientes a los personajes y los parámetros a pasar
 */
interface CharactersService {

    @GET("v1/public/characters")
    suspend fun getCharacters(@Query("nameStartsWith") nameStart: String?,
                              @Query("modifiedSince") date: String?,
                              @Query("orderBy") orderBy: String?,
                              @Query("limit") limit: Int? = 50,
                              @Query("offset") offset: Int? = 0): Response<CharacterDataWrapper>

    @GET("/v1/public/characters/{characterId}")
    suspend fun getCharacterDetail(@Path("characterId") characterId: Int): Response<CharacterDataWrapper>

    @GET("/v1/public/comics/{comicId}/characters")
    suspend fun getCharactersByComic(@Path("comicId") comicId: Int,
                                     @Query("nameStartsWith") nameStart: String?,
                                     @Query("modifiedSince") date: String?,
                                     @Query("orderBy") orderBy: String?,
                                     @Query("limit") limit: Int? = 50,
                                     @Query("offset") offset: Int? = 0): Response<CharacterDataWrapper>
}