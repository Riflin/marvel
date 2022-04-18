package com.entelgy.marvel.app.characterdetails.presenter

import android.content.Intent
import android.widget.Toast
import com.entelgy.marvel.app.characterdetails.CharacterDetailsView
import com.entelgy.marvel.app.photos.PhotoActivity
import com.entelgy.marvel.data.model.*
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.data.model.characters.CharacterDataWrapper
import com.entelgy.marvel.data.model.characters.ComicSummary
import com.entelgy.marvel.data.model.imageformats.FullSizeImage
import com.entelgy.marvel.data.model.imageformats.LandscapeImage
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.domain.usecases.network.characters.GetCharacterDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class CharacterDetailsPresenterImpl : CharacterDetailsPresenter, CoroutineScope {
    override var view: CharacterDetailsView? = null
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
            val characterId = intent.getIntExtra(Constants.CHARACTER_ID, -1)
            character = intent.getParcelableExtra(Constants.CHARACTER)
            when {
                character != null -> {
                    showDataFromCharacter(character!!, null)
                }
                characterId != -1 -> {
                    downloadData(characterId)
                }
                else -> {
                    view?.onDataError()
                }
            }
        } else {
            view?.onDataError()
        }
    }

    private fun downloadData(characterId: Int) {
        launch {
            view?.showLoading()
            try {
                val result = GetCharacterDetails(characterId).downloadData()
                if (result.isSuccessful) {
                    view?.showLoading(false)
                    //Comprobamos el código que devuelve
                    val characterDataWrapper = result.body()
                    when (characterDataWrapper?.code) {
                        //Correcto
                        200 -> {
                            val charactersFound = characterDataWrapper.data?.count
                            val characterList = characterDataWrapper.data?.results
                            //Comprobamos que tenemos en la lista algún personaje (sólo
                            if (characterList != null && characterList.isNotEmpty() && charactersFound == 1) {
                                character = characterList[0]
                                //Volvemos a comprobar que efectivamente tenemos un personaje
                                character?.let { character ->
                                    showDataFromCharacter(character, characterDataWrapper)
                                } ?: run {
                                    view?.onCharacterNotFound()
                                }
                            } else {
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
                    val jsonObj = JSONObject(result.errorBody()?.charStream()?.readText() ?: "{\"msg\":\"\"}")
                    view?.showError(jsonObj.getString("msg"))
                }
            } catch (e: Exception) {
                view?.showError(e.message ?: "")
            }
        }
    }

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

    override fun showComics() {
        view?.showComics(character?.commics)
    }

    override fun showSeries() {
        view?.showSeries(character?.series)
    }

    override fun showStories() {
        view?.showStories(character?.stories)
    }

    override fun showEvents() {
        view?.showEvents(character?.events)
    }

    override fun showPhotoDetail() {
        character?.let { character ->
            view?.context?.startActivity(PhotoActivity.createNewIntent(view!!.context, character.getThumbnailPath(FullSizeImage())))
        }
    }

    override fun onComicSelected(comic: ComicSummary) {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onEventSelected(event: EventSummary) {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onSeriesSelected(series: SeriesSummary) {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onStorySelected(story: StorySummary) {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onMoreComicsSelected() {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onMoreEventSelected() {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onMoreSeriesSelected() {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onMoreStoriesSelected() {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onUrlSelected(url: Url) {
        view?.openUrl(url)
    }
}