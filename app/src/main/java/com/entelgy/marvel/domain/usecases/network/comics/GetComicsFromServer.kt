package com.entelgy.marvel.domain.usecases.network.comics

import com.entelgy.marvel.data.model.comics.ComicDataWrapper
import com.entelgy.marvel.data.providers.ComicsNetworkProvider
import com.entelgy.marvel.data.services.ServiceFactory
import com.entelgy.marvel.domain.NetworkUseCase
import retrofit2.Response

/**
 * Caso de uso para obtener un listado de todos los comics
 *
 * No se ha implementado esta funcionalidad
 */
class GetComicsFromServer: NetworkUseCase<ComicDataWrapper>() {
    override suspend fun downloadData(): Response<ComicDataWrapper> {
        return ComicsNetworkProvider(ServiceFactory.getComicsService()).getComics()
    }
}