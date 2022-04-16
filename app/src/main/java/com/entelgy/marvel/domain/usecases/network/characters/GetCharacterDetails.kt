package com.entelgy.marvel.domain.usecases.network.characters

import com.entelgy.marvel.data.model.characters.CharacterDataWrapper
import com.entelgy.marvel.data.providers.CharactersNetworkProvider
import com.entelgy.marvel.data.services.ServiceFactory
import com.entelgy.marvel.domain.NetworkUseCase
import retrofit2.Response

class GetCharacterDetails(private val characterId: Int): NetworkUseCase<CharacterDataWrapper>() {
    override suspend fun downloadData(): Response<CharacterDataWrapper> {
        return CharactersNetworkProvider(ServiceFactory.getCharactersService()).getCharacterDetail(characterId)
    }
}