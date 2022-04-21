package com.entelgy.marvel.app.characterdetails.presenter

import android.content.Intent
import android.widget.Toast
import com.entelgy.marvel.R
import com.entelgy.marvel.app.characterdetails.CharacterDetailsView
import com.entelgy.marvel.app.routing.Routing
import com.entelgy.marvel.data.model.EventSummary
import com.entelgy.marvel.data.model.SeriesSummary
import com.entelgy.marvel.data.model.StorySummary
import com.entelgy.marvel.data.model.Url
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.data.model.characters.CharacterDataWrapper
import com.entelgy.marvel.data.model.characters.ComicSummary
import com.entelgy.marvel.data.model.imageformats.FullSizeImage
import com.entelgy.marvel.data.model.imageformats.LandscapeImage
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.domain.usecases.network.characters.GetCharacterDetails
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CharacterDetailsPresenterImpl : CharacterDetailsPresenter, CoroutineScope {
    override var view: CharacterDetailsView? = null
    //Context de las corutinas, para ejecutar las llamadas en segundo plano
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val job = Job()
    private var character: Character? = null

    override fun create() {
        character = null
    }

    override fun resume() {
        //NOTHING
    }

    override fun pause() {
        //NOTHING
    }

    override fun stop() {
        //NOTHING
    }

    override fun destroy() {
        view = null
    }

    override fun getData(intent: Intent?) {
        if (intent != null) {
            //Vemos los datos que tenemos en el intent
            val characterId = intent.getIntExtra(Constants.CHARACTER_ID, -1)
            val characterName = intent.getStringExtra(Constants.CHARACTER_NAME) ?: ""
            character = intent.getParcelableExtra(Constants.CHARACTER)
            when {
                //Si ya tenemos el personaje completo, mostramos su datos tal cual
                character != null -> {
                    showDataFromCharacter(character!!, null)
                }
                //Si tenemos un id, nos tenemos que descargar la información
                characterId != -1 -> {
                    //Mostramos de primeras el nombre porque luego, al mostrarlo tras descargar los datos,
                    //no se actualizaba correctamente
                    view?.showName(characterName)
                    //Nos descargamos los datos del personaje a partir de su id
                    downloadData(characterId)
                }
                //Si no tenemos nada, pues avisamos de que ha fallado
                else -> {
                    view?.onDataError()
                }
            }
        } else {
            view?.onDataError()
        }
    }

    /**
     * Se descarga la información del personaje desde el servidor a partir de su id
     */
    private fun downloadData(characterId: Int) {
        //La descarga se hará en segundo plano, claro
        launch {
            //Mostramos el loading
            view?.showLoading()
            try {
                //Hacemos la llamada al servidor para descargar la info
                val result = GetCharacterDetails(characterId).downloadData()
                //Comprobamos que la llamada ha sido satisfactoria y nos ha devuelto los datos
                if (result.isSuccessful) {
                    //Ocultamos el loading
                    view?.showLoading(false)
                    //Comprobamos el código que devuelve
                    val characterDataWrapper = result.body()
                    when (characterDataWrapper?.code) {
                        //Correcto
                        200 -> {
                            val charactersFound = characterDataWrapper.data?.count
                            val characterList = characterDataWrapper.data?.results
                            //Comprobamos que tenemos en la lista algún personaje (sólo uno, en realidad)
                            if (characterList != null && characterList.isNotEmpty() && charactersFound == 1) {
                                character = characterList[0]
                                //Volvemos a comprobar que efectivamente tenemos un personaje
                                character?.let { character ->
                                    //Mostramos los datos del mismo
                                    showDataFromCharacter(character, characterDataWrapper)
                                } ?: run {
                                    //Si es null el personaje, pues error al canto
                                    view?.onCharacterNotFound()
                                }
                            } else {
                                //Si no tenemos personaje, o tenemos más de uno, indicamos el error
                                view?.onCharacterNotFound()
                            }
                        }
                        //Personaje no encontrado
                        404 -> {
                            view?.onCharacterNotFound()
                        }
                        null -> {
                            view?.onCharacterNotFound()
                        }
                        else -> {
                            view?.onCharacterNotFound()
                        }
                    }
                } else {
                    //Si la llamada ha fallado, nos devuelve un json con un campo "code" y otro "status" indicando el error.
                    //Nos quedamos sólo con el del status para indicar el fallo al usuario
                    //Para ello, parseamos el resultado a un JsonObject y cogemos ese campo
                    val errorBody = result.errorBody()?.string()
                    if (errorBody != null) {
                        val json = JsonParser.parseString(errorBody)
                        if (json is JsonObject) {
                            val status = json.get("status").asString
                            view?.showError(status)
                        } else {
                            view?.showError(view?.viewContext?.getString(R.string.error_downloading_character_details) ?: "")
                        }
                    } else {
                        view?.showError(view?.viewContext?.getString(R.string.error_downloading_character_details) ?: "")
                    }
                }
            } catch (e: Exception) {
                view?.showError(e.message ?: "")
            }
        }
    }

    /**
     * Llama a la vista para que vaya mostrando la información del personaje.
     * El CharacterDataWrapper es únicamente para el texto del copyright
     */
    private fun showDataFromCharacter(character: Character,
                                      characterDataWrapper: CharacterDataWrapper?) {
        //Mostramos la imagen
        view?.showImage(character.getThumbnailPath(LandscapeImage.Incredible))
        //Mostramos el nombre del personaje
        view?.showName(character.name ?: "")
        //Mostramos la bio.
        view?.showBio(character.description)
        //Mostramos el número de comics, etc
        view?.showNumberOfItems(
            character.commics?.available ?: 0,
            character.series?.available ?: 0,
            character.events?.available ?: 0,
            character.stories?.available ?: 0
        )
        //Mostramos el copyright
        view?.showCopyright(characterDataWrapper?.attributionText ?: Constants.COPYRIGHT_TEXT)
        //Mostramos los enlaces
        view?.showUrls(character.urls)
        //De primeras, mostramos los comics
        view?.showComics(character.commics)
    }

    /**
     * Muestra el listado de cómics donde aparece el personaje
     */
    override fun showComics() {
        view?.showComics(character?.commics)
    }

    /**
     * Muestra el listado de series en las que aparece el personaje
     */
    override fun showSeries() {
        view?.showSeries(character?.series)
    }

    /**
     * Muestra el listado de historias donde está el personaje
     */
    override fun showStories() {
        view?.showStories(character?.stories)
    }

    /**
     * Muestra el listado de eventos en los que aparece el personaje
     */
    override fun showEvents() {
        view?.showEvents(character?.events)
    }

    /**
     * Muestra la imagen a tamaño completo
     */
    override fun showPhotoDetail() {
        character?.let { character ->
            view?.viewContext?.let { context ->
                Routing.goToPhotoActivity(context, character.getThumbnailPath(FullSizeImage()))
            }
        }
    }

    /**
     * Muestra los detalles del cómic seleccionado
     */
    override fun onComicSelected(comic: ComicSummary) {
        comic.resourceURI?.let { uri ->
            //El id del comic está en la última parte de la uri (ej: http://gateway.marvel.com/v1/public/comics/4100)
            val urlSplit = uri.split("/")
            val id = urlSplit[urlSplit.size - 1].toInt()

            //Si tenemos context, abrimos la pantalla de detalles del cómic
            view?.viewContext?.let { context ->
                Routing.goToComicDetailsActivity(context, id)
            }
        }
    }

    /*
     * Todos estos métodos serían para mostrar los detalles de series, historias y eventos, pero
     * no los implementamos o la aplicación se haría gigante
     */
    override fun onEventSelected(event: EventSummary) {
        Toast.makeText(view?.viewContext, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onSeriesSelected(series: SeriesSummary) {
        Toast.makeText(view?.viewContext, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onStorySelected(story: StorySummary) {
        Toast.makeText(view?.viewContext, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    /**
     * Aquí me habría gustado crear una pantalla en la que poder ver todos los comics del personaje,
     * pudiendo filtrar con alguno de los filtros que ofrece la api, pero finalmente no se implementa
     */
    override fun onMoreComicsSelected() {
        Toast.makeText(view?.viewContext, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onMoreEventSelected() {
        Toast.makeText(view?.viewContext, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onMoreSeriesSelected() {
        Toast.makeText(view?.viewContext, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onMoreStoriesSelected() {
        Toast.makeText(view?.viewContext, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    /**
     * Abre un webview para mostrar el contenido de cada uno de los enlaces
     */
    override fun onUrlSelected(url: Url) {
        view?.viewContext?.let { context ->
            Routing.goToWebActivity(context, url)
        }
    }
}