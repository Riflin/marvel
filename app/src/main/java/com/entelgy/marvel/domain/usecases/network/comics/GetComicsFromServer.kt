package com.entelgy.marvel.domain.usecases.network.comics

import com.entelgy.marvel.data.model.comics.ComicDataWrapper
import com.entelgy.marvel.data.providers.ComicsNetworkProvider
import com.entelgy.marvel.data.services.ServiceFactory
import com.entelgy.marvel.domain.NetworkUseCase

class GetComicsFromServer: NetworkUseCase<ComicDataWrapper>() {
    override suspend fun downloadData(): ComicDataWrapper {
        return ComicsNetworkProvider(ServiceFactory.getComicsService()).getComics()
    }
}