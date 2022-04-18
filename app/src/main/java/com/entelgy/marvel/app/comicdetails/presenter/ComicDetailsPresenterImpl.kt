package com.entelgy.marvel.app.comicdetails.presenter

import android.content.Intent
import com.entelgy.marvel.app.comicdetails.ComicDetailsView
import com.entelgy.marvel.data.model.*
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.data.model.comics.Comic
import com.entelgy.marvel.data.model.comics.ComicDataWrapper
import com.entelgy.marvel.data.model.imageformats.PortraitImage
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.domain.usecases.network.comics.GetComicDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class ComicDetailsPresenterImpl : ComicDetailsPresenter, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val job = Job()

    override var view: ComicDetailsView? = null

    private var comic: Comic? = null

    override fun create() {
        comic = null
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

    override fun getData(intent: Intent?) {
        if (intent != null) {
            val comicId = intent.getIntExtra(Constants.COMIC_ID, -1)
            comic = intent.getParcelableExtra(Constants.COMIC)
            when {
                comic != null -> {
                    showDataFromComic(comic!!, null)
                }
                comicId != -1 -> {
                    downloadData(comicId)
                }
                else -> {
                    view?.onDataError()
                }
            }
        } else {
            view?.onDataError()
        }
    }

    private fun downloadData(comicId: Int) {
        launch {
            view?.showLoading()
            try {
                val result = GetComicDetails(comicId).downloadData()
                if (result.isSuccessful) {
                    view?.showLoading(false)
                    //Comprobamos el código que devuelve
                    val comicDataWrapper = result.body()
                    when (comicDataWrapper?.code) {
                        //Correcto
                        200 -> {
                            val comicsFound = comicDataWrapper.data?.count
                            val comicList = comicDataWrapper.data?.results
                            //Comprobamos que tenemos en la lista algún personaje (sólo
                            if (comicList != null && comicList.isNotEmpty() && comicsFound == 1) {
                                comic = comicList[0]
                                //Volvemos a comprobar que efectivamente tenemos un personaje
                                comic?.let { character ->
                                    showDataFromComic(character, comicDataWrapper)
                                } ?: run {
                                    view?.onComicNotFound()
                                }
                            } else {
                                view?.onComicNotFound()
                            }
                        }
                        //Personaje no encontrado
                        404 -> {
                            view?.onComicNotFound()
                        }
                        null -> {
                            view?.onComicNotFound()
                        }
                        else -> {
                            view?.onComicNotFound()
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

    private fun showDataFromComic(comic: Comic,
                                      comicDataWrapper: ComicDataWrapper?) {
        //Mostramos el título
        view?.showTitle(comic.title ?: "")
        //Mostramos la imagen
        view?.showImage(comic.getThumbnailPath(PortraitImage.Incredible))


    }

    override fun onCharacterSelected(character: Character) {
        TODO("Not yet implemented")
    }

    override fun onCharacterSelected(character: CharacterSummary) {
        TODO("Not yet implemented")
    }

    override fun onMoreCharactersSelected() {
        TODO("Not yet implemented")
    }

    override fun onEventSelected(event: EventSummary) {
        TODO("Not yet implemented")
    }

    override fun onMoreEventSelected() {
        TODO("Not yet implemented")
    }

    override fun onSeriesSelected(series: SeriesSummary) {
        TODO("Not yet implemented")
    }

    override fun onMoreSeriesSelected() {
        TODO("Not yet implemented")
    }

    override fun onStorySelected(story: StorySummary) {
        TODO("Not yet implemented")
    }

    override fun onMoreStoriesSelected() {
        TODO("Not yet implemented")
    }

    override fun onUrlSelected(url: Url) {
        TODO("Not yet implemented")
    }

    override fun showComics() {
        TODO("Not yet implemented")
    }

    override fun showSeries() {
        TODO("Not yet implemented")
    }

    override fun showStories() {
        TODO("Not yet implemented")
    }

    override fun showEvents() {
        TODO("Not yet implemented")
    }

    override fun showPhotoDetail() {
        TODO("Not yet implemented")
    }

    override fun showVariants() {
        TODO("Not yet implemented")
    }

    override fun showImages() {
        TODO("Not yet implemented")
    }
}