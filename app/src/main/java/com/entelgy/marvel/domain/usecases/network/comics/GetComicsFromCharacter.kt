package com.entelgy.marvel.domain.usecases.network.comics

import com.entelgy.marvel.data.model.comics.ComicDataWrapper
import com.entelgy.marvel.data.providers.ComicsNetworkProvider
import com.entelgy.marvel.data.services.ServiceFactory
import com.entelgy.marvel.domain.NetworkUseCase
import retrofit2.Response

/**
 * Caso de uso para obtener los cómics en los que aparece un personaje en particular
 *
 * (no se usa porque finalmente no se ha implementado esa funcionalidad)
 */
class GetComicsFromCharacter(private val characterId: Int): NetworkUseCase<ComicDataWrapper>() {
    override suspend fun downloadData(): Response<ComicDataWrapper> {
        return ComicsNetworkProvider(ServiceFactory.getComicsService()).getComicsFromCharacter(characterId)
    }
}