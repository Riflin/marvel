package com.entelgy.marvel.app.charactersbycomic.presenter

import android.content.Intent
import com.entelgy.marvel.R
import com.entelgy.marvel.app.charactersbycomic.CharactersByComicView
import com.entelgy.marvel.app.characterslist.presenter.CharactersListPresenterImpl
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.domain.usecases.network.characters.GetCharactersByComicFromServer
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Este presenter es una copia del CharactersListPresenterImpl, por lo que hereda de él los métodos comunes y
 * simplemente sobreescribe los necesarios para descargarse los datos propios de cada cómic
 */
class CharactersByComicPresenterImpl : CharactersListPresenterImpl<CharactersByComicView>(), CharactersByComicPresenter, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val job = Job()

    override var view: CharactersByComicView? = null

    private var comicID: Int = 0

    override fun getData(intent: Intent?) {
        if (intent != null) {
            comicID = intent.getIntExtra(Constants.COMIC_ID, -1)
            val comicName = intent.getStringExtra(Constants.COMIC) ?: ""
            //Comprobamos que realmente tenemos un id válido
            if (comicID != -1) {
                //Si tenemos id, mostramos el nombre del comic
                view?.showComicName(comicName)
                //Y, directamente, nos descargamos los personajes del cómic
                downloadCharacters()
            } else {
                view?.onDataError()
            }
        } else {
            view?.onDataError()
        }
    }

    override fun create() {
        sortName = null
        sortDate = null
        filterName = null
        filterDate = null
        lastDataFetched = null
        comicID = 0
    }


    override fun resume() {

    }

    override fun pause() {

    }

    override fun stop() {

    }

    override fun destroy() {
        view = null
    }

    override fun getDataFromServer() {
        downloadCharacters()
    }

    override fun getMoreCharacters() {
        //Sólo descargamos más personajes si tenemos algo más que descargar
        lastDataFetched?.let { lastDataFetched ->
            /* Tendremos algo más que descargar si el total de personajes es mayor al count (número de
             * personajes obtenidos en la llamada) sumado al offset (número de personajes "saltados") de
             * ese ultimo DataContainer
             */
            if (lastDataFetched.total ?: 0 > (lastDataFetched.count ?: 0).plus(lastDataFetched.offset ?: 0)) {
                downloadCharacters(loadMoreCharacters = true)
            }
        } ?: run {
            downloadCharacters(loadMoreCharacters = true)
        }
    }

    private fun downloadCharacters(loadMoreCharacters: Boolean = false) {
        launch {
            view?.showLoading()
            val sort = getSortParameter()
            try {
                /*Si vamos a descargar más personajes de los que ya teníamos, debemos utilizar los datos
                 * que ya teníamos para usar el offset y no volver a descargar los mismos personajes
                 * que ya teníamos
                 */
                val offset = if (loadMoreCharacters) {
                    (lastDataFetched?.offset ?: 0).plus(lastDataFetched?.count ?: 0)
                } else {
                    0
                }
                val result = GetCharactersByComicFromServer(comicID, filterName, filterDate, sort, offset).downloadData()

                //Comprobamos que la llamada ha sido correcta
                if (result.isSuccessful) {
                    view?.showLoading(false)
                    //Nos queadmos con CharacterDataContainer como último dato obtenido
                    val dataWrapper = result.body()
                    lastDataFetched = dataWrapper?.data
                    //Comprobamos que tenemos datos de verdad
                    lastDataFetched?.let {
                        //Mostramos el copyright
                        view?.showCopyright(dataWrapper!!.attributionText)
                        //Si estamos descargando más personajes, los añadimos
                        if (loadMoreCharacters) {
                            view?.addCharacters(it.results)
                        } else {
                            //Si no, los mostramos desde 0
                            view?.showCharacters(it.results)
                        }
                    }
                } else {
                    //Si ha fallado la llamada, mostramos el error
                    view?.showLoading(false)
                    //Obtenemos el error
                    val errorBody = result.errorBody()?.string()
                    if (errorBody != null) {
                        val json = JsonParser.parseString(errorBody)
                        if (json is JsonObject) {
                            val status = json.get("status").asString
                            view?.showError(status)
                        } else {
                            view?.showError(view?.viewContext?.getString(R.string.error_downloading_characters) ?: "")
                        }
                    } else {
                        view?.showError(view?.viewContext?.getString(R.string.error_downloading_characters) ?: "")
                    }
                }
            } catch (e: Exception) {
                view?.showLoading(false)
                view?.showError(e.message ?: view?.viewContext?.getString(R.string.error_downloading_characters) ?: "")
            }
        }
    }
}