package com.entelgy.marvel.domain.usecases.network.comics

import com.entelgy.marvel.data.model.comics.ComicDataWrapper
import com.entelgy.marvel.data.providers.ComicsNetworkProvider
import com.entelgy.marvel.data.services.ServiceFactory
import com.entelgy.marvel.domain.NetworkUseCase
import retrofit2.Response

//Caso de uso para obtener los detalles de un cómic en particular
class GetComicDetails(private val comicId: Int): NetworkUseCase<ComicDataWrapper>() {
    override suspend fun downloadData(): Response<ComicDataWrapper> {
        return ComicsNetworkProvider(ServiceFactory.getComicsService()).getComicDetails(comicId)
    }
}