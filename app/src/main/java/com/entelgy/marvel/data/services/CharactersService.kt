package com.entelgy.marvel.data.services

import com.entelgy.marvel.data.model.characters.CharacterDataWrapper
import retrofit2.http.GET
import retrofit2.http.Path

interface CharactersService {

    @GET("v1/public/characters")
    suspend fun getCharacters(): CharacterDataWrapper

    @GET("/v1/public/characters/{characterId}")
    suspend fun getCharacterDetail(@Path("characterId") characterId: Int): CharacterDataWrapper
}