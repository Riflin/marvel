package com.entelgy.marvel.data.providers

import com.entelgy.marvel.data.model.characters.CharacterDataWrapper
import com.entelgy.marvel.data.services.CharactersService
import retrofit2.Response

class CharactersNetworkProvider(private val api: CharactersService) {

    suspend fun getCharacters(name: String?, date: String?, orderBy: String?,
                              limit: Int? = 40, offset: Int? = 0): Response<CharacterDataWrapper> {
        return api.getCharacters(name, date, orderBy, limit, offset)
    }

    suspend fun getCharacterDetail(characterId: Int): Response<CharacterDataWrapper> {
        return api.getCharacterDetail(characterId)
    }

    suspend fun getCharactersByComic(comicID: Int, name: String?, date: String?, orderBy: String?,
                              limit: Int? = 40, offset: Int? = 0): Response<CharacterDataWrapper> {
        return api.getCharactersByComic(comicID, name, date, orderBy, limit, offset)
    }
}