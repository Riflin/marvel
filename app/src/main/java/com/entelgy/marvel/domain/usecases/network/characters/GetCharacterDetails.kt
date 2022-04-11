package com.entelgy.marvel.domain.usecases.network.characters

import com.entelgy.marvel.data.model.characters.CharacterDataWrapper
import com.entelgy.marvel.data.providers.CharactersNetworkProvider
import com.entelgy.marvel.data.services.ServiceFactory
import com.entelgy.marvel.domain.NetworkUseCase

class GetCharacterDetails(private val characterId: Int): NetworkUseCase<CharacterDataWrapper>() {
    override suspend fun downloadData(): CharacterDataWrapper {
        return CharactersNetworkProvider(ServiceFactory.getCharactersService()).getCharacterDetail(characterId)
    }
}