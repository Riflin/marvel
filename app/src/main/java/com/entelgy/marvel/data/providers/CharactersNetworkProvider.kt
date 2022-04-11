package com.entelgy.marvel.data.providers

import com.entelgy.marvel.data.model.characters.CharacterDataWrapper
import com.entelgy.marvel.data.services.CharactersService

class CharactersNetworkProvider(private val api: CharactersService) {

    suspend fun getCharacters(): CharacterDataWrapper {
        return api.getCharacters()
    }
    suspend fun getCharacterDetail(characterId: Int): CharacterDataWrapper {
        return api.getCharacterDetail(characterId)
    }
}