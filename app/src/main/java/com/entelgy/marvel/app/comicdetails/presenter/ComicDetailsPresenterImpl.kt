package com.entelgy.marvel.app.comicdetails.presenter

import android.content.Intent
import android.widget.Toast
import com.entelgy.marvel.app.characterdetails.CharacterDetailsActivity
import com.entelgy.marvel.app.comicdetails.ComicDetailsView
import com.entelgy.marvel.app.routing.Routing
import com.entelgy.marvel.data.model.*
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.data.model.characters.ComicSummary
import com.entelgy.marvel.data.model.comics.Comic
import com.entelgy.marvel.data.model.comics.ComicDataWrapper
import com.entelgy.marvel.data.model.imageformats.FullSizeImage
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
                    view?.showLoading(false)
                    view?.onErrorParsingData(jsonObj.getString("msg"))
                }
            } catch (e: Exception) {
                view?.showLoading(false)
                view?.onErrorParsingData(e.message ?: "")
            }
        }
    }

    private fun showDataFromComic(comic: Comic,
                                      comicDataWrapper: ComicDataWrapper?) {
        //Mostramos el título
        view?.showTitle(comic.title ?: "")
        //Mostramos la imagen
        view?.showImage(comic.getThumbnailPath(FullSizeImage()))
        //Fecha de publicación
        view?.showPublishedDate(comic.getPublicationDate())
        //Autor(es)
        //Primero, los cogemos usando la función definida en el objeto
        val authors = comic.getCreatorsByRole(Role.WRITER)
        view?.showAuthors(authors)
        //Dibujantes
        //Los dibujantes pueden ser "penciler" o "penciller", así que cogemos ambos
        val pencilers = ArrayList(comic.getCreatorsByRole(Role.PENCILER))
        pencilers.addAll(comic.getCreatorsByRole(Role.PENCILLER))
        view?.showPencillers(pencilers)
        //Descripción
        view?.showDescription(comic.getPreferredDescription())
        //Número del comic
        view?.showNumber(comic.issueNumber)
        //Indicamos si hay imágenes promocionales o no
        view?.showImagesAvailable(comic.images.isNotEmpty())
        view?.showNumberOfImagesAvailable(comic.images.size)
        //Indicamos si hay variantes disponibles o no
        view?.showVariantsAvailable(comic.variants.isNotEmpty())
        view?.showNumberOfVariantsAvailable(comic.variants.size)
        //Formato
        view?.showFormat(comic.format)
        //Número de páginas
        view?.showPageCount(comic.pageCount)
        //Precio(s)
        view?.showPrice(comic.prices)
        //Código UPC
        view?.showUPC(comic.upc)
        //Código ISBN
        view?.showISBN(comic.isbn)
        //Código EAN
        view?.showEAN(comic.ean)
        //Código ISSN
        view?.showISSN(comic.issn)
        //Código diamond
        view?.showDiamondCode(comic.diamondCode)
        //Fecha de fin de pedidos
        view?.showFOCDate(comic.getFocDate())
        //Fecha de inclusión en Marvel Unlimited
        view?.showUnlimitedDate(comic.getMarvelUnlimitedDate())
        //Fecha de posibilidad de compra digital
        view?.showDigitalPurchaseDate(comic.getDigitalPurchaseDate())
        //Entintadores
        view?.showInkers(comic.getCreatorsByRole(Role.INKER))
        //Coloristas
        view?.showColorists(comic.getCreatorsByRole(Role.COLORIST))
        //Rotuladores
        view?.showLetterers(comic.getCreatorsByRole(Role.LETTERER))
        //Pintores
        view?.showPainters(comic.getCreatorsByRole(Role.PAINTER))
        //Editores
        view?.showEditors(comic.getCreatorsByRole(Role.EDITOR))
        //Ahora vemos si tenemos info de la portada
        val coverPencilers = ArrayList(comic.getCreatorsByRole(Role.PENCILER_COVER))
        coverPencilers.addAll(comic.getCreatorsByRole(Role.PENCILLER_COVER))
        val coverColorists = comic.getCreatorsByRole(Role.COLORIST_COVER)
        val coverInkers = comic.getCreatorsByRole(Role.INKER_COVER)
        val coverPainters = comic.getCreatorsByRole(Role.PAINTER_COVER)
        val coverEditors = comic.getCreatorsByRole(Role.EDITOR_COVER)
        //Si todas las listas de la portada están vacías, indicamos que no tenemos info disponible
        if (coverPencilers.isEmpty() && coverColorists.isEmpty() && coverInkers.isEmpty()
            && coverPainters.isEmpty() && coverEditors.isEmpty()) {
            view?.showNoCoverInfo()
        } else {
            view?.showNoCoverInfo(false)
            view?.showCoverPencilers(coverPencilers)
            view?.showCoverColorists(coverColorists)
            view?.showCoverInkers(coverInkers)
            view?.showCoverPainters(coverPainters)
            view?.showCoverEditors(coverEditors)
        }
        //Serie a la que pertenece el cómic
        view?.showSeries(comic.series)
        //Colecciones en las que aparece
        view?.showCollections(comic.collections)
        //Cómics que aparecen en esta colección
        view?.showCollectedIssues(comic.collectedIssues)
        //Número de personajes, series, etc. de este cómic
        val numberOfCharacters = comic.characters?.available ?: 0
        val numberOfEvents = comic.events?.available ?: 0
        val numberOfStories = comic.stories?.available ?: 0
        view?.showNumberOfItems(numberOfCharacters, numberOfEvents, numberOfStories)
        //De primeras, mostramos los personajes del cómic
        view?.showCharacters(comic.characters)

        //Mostramos los posibles enlaces que tenga
        view?.showUrls(comic.urls)
        //Por último, el copyright
        view?.showCopyright(comicDataWrapper?.copyright ?: Constants.COPYRIGHT_TEXT)
    }

    override fun onComicSelected(comic: ComicSummary) {
        comic.resourceURI?.let { uri ->
            //El id del comic está en la última parte de la uri (ej: http://gateway.marvel.com/v1/public/comics/4100)
            val urlSplit = uri.split("/")
            val id = urlSplit[urlSplit.size-1].toInt()

            //TODO Hacerlo en un routing!!!
            view?.context?.let { context ->
                Routing.goToComicDetailsActivity(context, id)
            }
        }
    }

    override fun onMoreComicsSelected() {
        Toast.makeText(view?.context, "NOT YET STRING", Toast.LENGTH_LONG).show()
    }

    override fun onCharacterSelected(character: Character) {
        view?.context?.let { context ->
            Routing.goToCharacterDetailsActivity(context, character)
        }
    }

    override fun onCharacterSelected(character: CharacterSummary) {
        //El id del comic está en la última parte de la uri (ej: http://gateway.marvel.com/v1/public/characters/1009257)
        character.resourceURI?.let { uri ->
            val urlSplit = uri.split("/")
            val id = urlSplit[urlSplit.size-1].toInt()
            view?.context?.let { context ->
                Routing.goToCharacterDetailsActivity(context, id)
            }
        }
    }

    override fun onMoreCharactersSelected() {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onEventSelected(event: EventSummary) {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onMoreEventSelected() {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onStorySelected(story: StorySummary) {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onMoreStoriesSelected() {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onUrlSelected(url: Url) {
        //Sólo podremos abrir la activity si tenemos contexto
        view?.context?.let {context ->
            Routing.goToWebActivity(context, url)
        }
    }

    override fun showCoverImageDetail() {
        comic?.let { comic ->
            view?.context?.let { context ->
                Routing.goToPhotoActivity(context, comic.getThumbnailPath(FullSizeImage()))
            }
        }
    }

    override fun showCharacters() {
        view?.showCharacters(comic?.characters)
    }

    override fun showStories() {
        view?.showStories(comic?.stories)
    }

    override fun showEvents() {
        view?.showEvents(comic?.events)
    }

    override fun showPhotoDetail() {
        comic?.let { comic ->
            view?.context?.let { context ->
                Routing.goToPhotoActivity(context, comic.getThumbnailPath(FullSizeImage()))
            }
        }
    }

    override fun showVariants() {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun showPromotionalImages() {
        comic?.let { comic ->
            view?.context?.let { context ->
                Routing.goToPhotoListActivity(context, comic.images)
            }
        }
    }
}