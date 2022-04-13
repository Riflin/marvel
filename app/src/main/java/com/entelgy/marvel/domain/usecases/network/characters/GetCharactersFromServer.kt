package com.entelgy.marvel.domain.usecases.network.characters

import com.entelgy.marvel.data.model.characters.CharacterDataWrapper
import com.entelgy.marvel.data.providers.CharactersNetworkProvider
import com.entelgy.marvel.data.services.ServiceFactory
import com.entelgy.marvel.domain.NetworkUseCase
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class GetCharactersFromServer(private val name: String?, private val fecha: Date?,
                              private val orderBy: String?): NetworkUseCase<Response<CharacterDataWrapper>>() {
    override suspend fun downloadData(): Response<CharacterDataWrapper> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: String = if (fecha != null) sdf.format(fecha) else ""
        return CharactersNetworkProvider(ServiceFactory.getCharactersService()).getCharacters(name, date, orderBy)
    }
}