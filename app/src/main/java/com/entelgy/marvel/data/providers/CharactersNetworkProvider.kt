package com.entelgy.marvel.data.providers

import com.entelgy.marvel.data.model.characters.CharacterDataWrapper
import com.entelgy.marvel.data.services.CharactersService
import retrofit2.Response

class CharactersNetworkProvider(private val api: CharactersService) {

    suspend fun getCharacters(name: String?, date: String?, orderBy: String?): Response<CharacterDataWrapper> {
        return api.getCharacters(name, date, orderBy)
    }
    suspend fun getCharacterDetail(characterId: Int): CharacterDataWrapper {
        return api.getCharacterDetail(characterId)
    }
}