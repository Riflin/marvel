package com.entelgy.marvel.domain

import retrofit2.Response

/**
 * Clase base para los casos de uso que descargan información del servidor
 */
abstract class NetworkUseCase<T> {

    /**
     * Todas nuestras llamadas devuelve el objeto en un Response. Podríamos haber optado por
     * trabajar con observables pero finalmente preferimos esto
     */
    abstract suspend fun downloadData(): Response<T>
}