package com.entelgy.marvel.data.providers

import com.entelgy.marvel.data.model.characters.CharacterDataWrapper
import com.entelgy.marvel.data.services.CharactersService
import retrofit2.Response

/**
 * Provider para obtener los datos de los personajes desde el servidor
 */
class CharactersNetworkProvider(private val api: CharactersService) {

    /**
     * Info de los personajes. El límite lo dejamos por defecto en 40 para que vayan fluidas las llamadas
     */
    suspend fun getCharacters(name: String?, date: String?, orderBy: String?,
                              limit: Int? = 40, offset: Int? = 0): Response<CharacterDataWrapper> {
        return api.getCharacters(name, date, orderBy, limit, offset)
    }

    /**
     * Detalles del personaje
     */
    suspend fun getCharacterDetail(characterId: Int): Response<CharacterDataWrapper> {
        return api.getCharacterDetail(characterId)
    }

    /**
     * Personajes que aparecen en un cómic
     */
    suspend fun getCharactersByComic(comicID: Int, name: String?, date: String?, orderBy: String?,
                              limit: Int? = 40, offset: Int? = 0): Response<CharacterDataWrapper> {
        return api.getCharactersByComic(comicID, name, date, orderBy, limit, offset)
    }
}