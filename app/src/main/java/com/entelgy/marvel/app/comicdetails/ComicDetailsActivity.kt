package com.entelgy.marvel.app.comicdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.entelgy.marvel.R
import com.entelgy.marvel.app.characterdetails.adapter.EventSummaryAdapter
import com.entelgy.marvel.app.characterdetails.adapter.StorySummaryAdapter
import com.entelgy.marvel.app.characterdetails.adapter.UrlAdapter
import com.entelgy.marvel.app.comicdetails.adapter.CharacterSummaryAdapter
import com.entelgy.marvel.app.comicdetails.adapter.ComicCollectionAdapter
import com.entelgy.marvel.app.comicdetails.presenter.ComicDetailsPresenter
import com.entelgy.marvel.app.comicdetails.presenter.ComicDetailsPresenterImpl
import com.entelgy.marvel.app.presenter.PresenterFactory
import com.entelgy.marvel.app.utils.AppUtils
import com.entelgy.marvel.app.utils.base.BaseActivity
import com.entelgy.marvel.data.model.*
import com.entelgy.marvel.data.model.characters.ComicSummary
import com.entelgy.marvel.data.model.comics.Comic
import com.entelgy.marvel.data.model.comics.ComicPrice
import com.entelgy.marvel.data.model.comics.CreatorSummary
import com.entelgy.marvel.data.model.comics.PriceType
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.databinding.ActivityComicDetailsBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pantalla para mostrar los detalles de un cómic en concreto
 */
class ComicDetailsActivity: BaseActivity(), ComicDetailsView {

    companion object {

        /**
         * Abre la pantalla con un id para descargarse la información
         */
        fun createNewIntent(context: Context, comicId: Int): Intent {
            return Intent(context, ComicDetailsActivity::class.java).apply {
                putExtra(Constants.COMIC_ID, comicId)
            }
        }

        /**
         * Abre la pantalla con un cómic para mostrar los datos directamente
         */
        fun createNewIntent(context: Context, comic: Comic): Intent {
            return Intent(context, ComicDetailsActivity::class.java).apply {
                putExtra(Constants.COMIC_ID, comic.id)
                putExtra(Constants.COMIC, comic)
            }
        }
    }

    private lateinit var binding: ActivityComicDetailsBinding

    private lateinit var presenter: ComicDetailsPresenter

    //Formato con el que mostraremos las fechas
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityComicDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        presenter.getData(intent)

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    /**
     * Inicializamos el presenter
     */
    override fun init() {
        presenter = PresenterFactory.getComicDetailPresenter(this)
//        presenter.view = this
        presenter.create()
    }

    /**
     * Inicializamos las vistas
     */
    override fun initViews() {
        //ActionBar con la flechita hacia atrás
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //Tanto las colecciones, como los cómics de la colección, como los items y las urls, en una lista vertical
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCollections.layoutManager = layoutManager

        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCollectedIssues.layoutManager = layoutManager2

        val layoutManager3 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvItems.layoutManager = layoutManager3

        val layoutManager4 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvUrls.layoutManager = layoutManager4
    }

    override fun attachListenersToTheViews() {
        binding.ivCover.setOnClickListener {
            presenter.showCoverImageDetail()
        }
        binding.tvImages.setOnClickListener {
            presenter.showPromotionalImages()
        }
        binding.tvVariants.setOnClickListener {
            presenter.showVariants()
        }
        binding.tvCharacters.setOnClickListener {
            presenter.showCharacters()
        }
        binding.tvStories.setOnClickListener {
            presenter.showStories()
        }
        binding.tvEvents.setOnClickListener {
            presenter.showEvents()
        }
        binding.tvVerMoreDetails.setOnClickListener {
            toggleMoreDetailsVisibility()
        }
    }

    /**
     * Como en la pantalla del listado de personajes, esta función es exclusiva de la vista
     * y no tiene que pasar por el presenter
     */
    private fun toggleMoreDetailsVisibility() {
        if (binding.clMoreInfo.visibility == View.VISIBLE) {
            binding.clMoreInfo.visibility = View.GONE
            binding.tvVerMoreDetails.text = getString(R.string.ver_mas_detalles)
        } else {
            binding.clMoreInfo.visibility = View.VISIBLE
            binding.tvVerMoreDetails.text = getString(R.string.ocultar_mas_detalles)
        }
    }

    /**
     * Muestra el título del cómic
     */
    override fun showTitle(title: String) {
        binding.tvTitle.text = title
    }

    /**
     * Muestra la portada del cómic
     */
    override fun showImage(path: String) {
        //El progress mientras cargamos la imagen, siempre presente
        binding.progressImage.visibility = View.VISIBLE
        Picasso.get().load(path).into(binding.ivCover, object: Callback {
            override fun onSuccess() {
                binding.progressImage.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                binding.progressImage.visibility = View.GONE
                binding.ivCover.setImageResource(R.drawable.ic_broken_image)
            }
        })
    }

    override fun showPublishedDate(date: Date?) {
        //Si no tenemos fecha de publicación, indicamos "no disponible"
        if (date != null) {
            binding.tvPublished.text = sdf.format(date)
        } else {
            binding.tvPublished.text = getString(R.string.fecha_no_disponible)
        }
    }

    override fun showAuthors(authors: List<CreatorSummary>) {
        //Mostramos "autor" o "autores" en función del número de autores que tengamos
        binding.tvTextWriter.text = resources.getQuantityString(R.plurals.writer, authors.size)
        if (authors.isNotEmpty()) {
            val autores = getCreatorsList(authors)
            //Y los nombres de los autores
            binding.tvWriter.text = autores
        } else {
            binding.tvWriter.text = getString(R.string.no_author_info)
        }
    }

    /**
     * Aquí creamos una String con los nombres de cada uno de los creadores, separados por comas.
     * Es común para todos los creadores, indistintamente de su rol
     */
    private fun getCreatorsList(creators: List<CreatorSummary>): String {
        var autores = ""
        for (author in creators.withIndex()) {
            autores = autores.plus(author.value.name)

            //Vamos separando los autores por comas
            if (author.index < creators.lastIndex)
                autores = autores.plus(", ")
        }
        return autores
    }

    /**
     * Dibujantes del cómic
     */
    override fun showPencillers(pencillers: List<CreatorSummary>) {
        //Mostramos "dibujante" o "dibujantes" en función del número de autores que tengamos
        binding.tvTextPenciller.text = resources.getQuantityString(R.plurals.penciller, pencillers.size)
        if (pencillers.isNotEmpty()) {
            val pencilers = getCreatorsList(pencillers)

            binding.tvPenciller.text = pencilers
        } else {
            binding.tvPenciller.text = getString(R.string.no_author_info)
        }
    }

    /**
     * Descripción del cómic
     */
    override fun showDescription(description: String?) {
        //Mostramos que no hay descripción si ésta es nula o una cadena vacía
        val description1 = if (description.isNullOrBlank()) {
            getString(R.string.no_description_available)
        } else {
            description
        }
        binding.tvDescription.text = HtmlCompat.fromHtml(description1, HtmlCompat.FROM_HTML_MODE_LEGACY)

    }

    //Mostramos u ocultamos el textView para ver las imágenes promocionales
    override fun showImagesAvailable(show: Boolean) {
        binding.clImages.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Número de imágenes promocionales
     */
    override fun showNumberOfImagesAvailable(number: Int) {
        binding.tvImages.text = getString(R.string.images_available, number)
    }

    //Mostramos u ocultamoas el textView para ver las variantes del cómic
    override fun showVariantsAvailable(show: Boolean) {
        binding.clVariants.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Número de variantes del cómic
     */
    override fun showNumberOfVariantsAvailable(number: Int) {
        binding.tvVariants.text = getString(R.string.variantes_disponibles, number)
    }

    /**
     * Mostramos la serie a la que pertenece el cómic (o la ocultamos, si no pertenece a ninguna)
     */
    override fun showSeries(serie: SeriesSummary?) {
        if (serie != null) {
            binding.clSeries.visibility = View.VISIBLE
            binding.tvSeries.text = serie.name
        } else {
            binding.clSeries.visibility = View.GONE
        }
    }

    /**
     * Colecciones en las que aparece el cómic
     */
    override fun showCollections(collections: List<ComicSummary>) {
        if (collections.isNotEmpty()) {
            val adapter = ComicCollectionAdapter(this, collections, presenter)
            binding.llCollections.visibility = View.VISIBLE
            binding.rvCollections.adapter = adapter
        } else {
            binding.llCollections.visibility = View.GONE
        }
    }

    /**
     * Cómics pertenecientes a esta colección
     */
    override fun showCollectedIssues(collectedIssues: List<ComicSummary>) {
        if (collectedIssues.isNotEmpty()) {
            binding.llCollectedIssues.visibility = View.VISIBLE
            val adapter = ComicCollectionAdapter(this, collectedIssues, presenter)
            binding.rvCollectedIssues.adapter = adapter
        } else {
            binding.llCollectedIssues.visibility = View.GONE
        }
    }

    /**
     * Número del cómic
     */
    override fun showNumber(number: Int?) {
        if (number != null) {
            binding.tvNumber.text = number.toString()
        } else {
            binding.clNumber.visibility = View.GONE
        }
    }

    /**
     * Formato del cómic
     */
    override fun showFormat(format: String?) {
        if (format != null && format.isNotBlank()) {
            binding.tvFormat.text = format
        } else {
            binding.clFormat.visibility = View.GONE
        }
    }

    /**
     * Número de páginas
     */
    override fun showPageCount(pageCount: Int?) {
        if (pageCount != null) {
            binding.tvPageCount.text = pageCount.toString()
        } else {
            binding.clPageCount.visibility = View.GONE
        }
    }

    /**
     * Lista de precios
     */
    override fun showPrice(prices: List<ComicPrice>) {
        if (prices.isNotEmpty()) {
            binding.clPrice.visibility = View.VISIBLE
            //Mostramos "precio" o "precios" en función de los que tengamos
            binding.tvTextPrice.text = resources.getQuantityString(R.plurals.price, prices.size)
            var preciosAMostrar= ""
            for (price in prices.withIndex()) {
                //Miramoas el tipo de precio que es
                val tipoPrecio = PriceType.getPryceTipe(price.value.type)
                //Cogemos el string correspondiente al precio o el tipo que sea si no lo tenemos
                val tipoPrecioFormatted = if (tipoPrecio != null) getString(tipoPrecio.text) else price.value.type
                //Mostramos el valor del precio junto al tipo
                //Si el precio es 0, mostraremos "Gratis"
                val priceFormatted = if (price.value.price ?: 0f > 0) {
                    getString(R.string.price_formatted, price.value.price, tipoPrecioFormatted)
                } else {
                    getString(R.string.free_price_formatted, tipoPrecioFormatted)
                }
                //Lo añadimos a la cadena de precios a mostrar
                preciosAMostrar = preciosAMostrar.plus(priceFormatted)
                //Si no es el último, añadimos un intro
                if (price.index < prices.lastIndex) {
                    preciosAMostrar += "\n"
                }
            }
            binding.tvPrice.text = preciosAMostrar
        } else {
            binding.clPrice.visibility = View.GONE
        }
    }

    override fun showUPC(upc: String?) {
        //Dependiendo de si tenemos código o no, mostramos su etiqueta o la ocultamos
        if (upc.isNullOrBlank()) {
            binding.clUPC.visibility = View.GONE
        } else {
            binding.clUPC.visibility = View.VISIBLE
            binding.tvUPC.text = upc
        }
    }

    override fun showISBN(isbn: String?) {
        //Dependiendo de si tenemos código o no, mostramos su etiqueta o la ocultamos
        if (isbn.isNullOrBlank()) {
            binding.clISBN.visibility = View.GONE
        } else {
            binding.tvISBN.text = isbn
            binding.clISBN.visibility = View.VISIBLE
        }
    }

    override fun showEAN(ean: String?) {
        //Dependiendo de si tenemos código o no, mostramos su etiqueta o la ocultamos
        if (ean.isNullOrBlank()) {
            binding.clEAN.visibility = View.GONE
        } else {
            binding.clEAN.visibility = View.VISIBLE
            binding.tvEAN.text = ean
        }
    }

    override fun showISSN(issn: String?) {
        //Dependiendo de si tenemos código o no, mostramos su etiqueta o la ocultamos
        if (issn.isNullOrBlank()) {
            binding.clISSN.visibility = View.GONE
        } else {
            binding.clISSN.visibility = View.VISIBLE
            binding.tvISSN.text = issn
        }
    }

    override fun showDiamondCode(code: String?) {
        //Dependiendo de si tenemos código o no, mostramos su etiqueta o la ocultamos
        if (code.isNullOrBlank()) {
            binding.clDiamond.visibility = View.GONE
        } else {
            binding.tvDiamond.text = code
            binding.clDiamond.visibility = View.VISIBLE
        }
    }

    override fun showFOCDate(date: Date?) {
        //Dependiendo de si tenemos fecha o no, mostramos su etiqueta o la ocultamos
        if (date != null) {
            binding.clFOCDate.visibility = View.VISIBLE
            binding.tvFOCDate.text = sdf.format(date)
        } else {
            binding.clFOCDate.visibility = View.GONE
        }
    }

    override fun showUnlimitedDate(date: Date?) {
        //Dependiendo de si tenemos fecha o no, mostramos su etiqueta o la ocultamos
        if (date != null) {
            binding.clUnlimitedDate.visibility = View.VISIBLE
            binding.tvUnlimitedDate.text = sdf.format(date)
        } else {
            binding.clUnlimitedDate.visibility = View.GONE
        }
    }

    override fun showDigitalPurchaseDate(date: Date?) {
        //Dependiendo de si tenemos fecha o no, mostramos su etiqueta o la ocultamos
        if (date != null) {
            binding.clDigitalPurchaseDate.visibility = View.VISIBLE
            binding.tvDigitalPurchaseDate.text = sdf.format(date)
        } else {
            binding.clDigitalPurchaseDate.visibility = View.GONE
        }
    }

    /**
     * Entintadores
     */
    override fun showInkers(inkers: List<CreatorSummary>) {
        if (inkers.isNotEmpty()) {
            val creators = getCreatorsList(inkers)

            binding.clInkers.visibility = View.VISIBLE
            //Mostramos la etiqueta en singular o plurar en función del número de creadores
            binding.tvTextInkers.text = resources.getQuantityString(R.plurals.inker, inkers.size)
            binding.tvInkers.text = creators
        } else {
            binding.clInkers.visibility = View.GONE
        }
    }

    /**
     * Coloristas
     */
    override fun showColorists(colorists: List<CreatorSummary>) {
        if (colorists.isNotEmpty()) {
            val creators = getCreatorsList(colorists)

            binding.clColorists.visibility = View.VISIBLE
            //Mostramos la etiqueta en singular o plurar en función del número de creadores
            binding.tvTextColorists.text = resources.getQuantityString(R.plurals.colorist, colorists.size)
            binding.tvColorists.text = creators
        } else {
            binding.clColorists.visibility = View.GONE
        }
    }

    /**
     * Rotulistas
     */
    override fun showLetterers(letterers: List<CreatorSummary>) {
        if (letterers.isNotEmpty()) {
            val creators = getCreatorsList(letterers)

            binding.clLetterers.visibility = View.VISIBLE
            //Mostramos la etiqueta en singular o plurar en función del número de creadores
            binding.tvTextLetterers.text = resources.getQuantityString(R.plurals.letterer, letterers.size)
            binding.tvLetterers.text = creators
        } else {
            binding.clLetterers.visibility = View.GONE
        }
    }

    /**
     * Pintores
     */
    override fun showPainters(painters: List<CreatorSummary>) {
        if (painters.isNotEmpty()) {
            val creators = getCreatorsList(painters)

            binding.clPainters.visibility = View.VISIBLE
            //Mostramos la etiqueta en singular o plurar en función del número de creadores
            binding.tvTextPainters.text = resources.getQuantityString(R.plurals.painter, painters.size)
            binding.tvPainters.text = creators
        } else {
            binding.clPainters.visibility = View.GONE
        }
    }

    /**
     * Editores
     */
    override fun showEditors(editors: List<CreatorSummary>) {
        if (editors.isNotEmpty()) {
            val creators = getCreatorsList(editors)

            binding.clEditors.visibility = View.VISIBLE

            //Mostramos la etiqueta en singular o plurar en función del número de creadores
            binding.tvTextEditors.text = resources.getQuantityString(R.plurals.editor, editors.size)
            binding.tvEditors.text = creators
        } else {
            binding.clEditors.visibility = View.GONE
        }
    }

    /**
     * Dibujantes de la portada
     */
    override fun showCoverPencilers(pencilers: List<CreatorSummary>) {
        if (pencilers.isNotEmpty()) {
            val creators = getCreatorsList(pencilers)

            binding.clPencilersCover.visibility = View.VISIBLE

            //Mostramos la etiqueta en singular o plurar en función del número de creadores
            binding.tvTextPencilersCover.text = resources.getQuantityString(R.plurals.penciller_cover, pencilers.size)
            binding.tvPencilersCover.text = creators
        } else {
            binding.clPencilersCover.visibility = View.GONE
        }
    }

    /**
     * Entintadore de la portada
     */
    override fun showCoverInkers(inkers: List<CreatorSummary>) {
        if (inkers.isNotEmpty()) {
            val creators = getCreatorsList(inkers)

            binding.clInkersCover.visibility = View.VISIBLE
            //Mostramos la etiqueta en singular o plurar en función del número de creadores
            binding.tvTextInkersCover.text = resources.getQuantityString(R.plurals.inker_cover, inkers.size)
            binding.tvInkersCover.text = creators
        } else {
            binding.clInkersCover.visibility = View.GONE
        }
    }

    /**
     * Coloristas de la portada
     */
    override fun showCoverColorists(colorists: List<CreatorSummary>) {
        if (colorists.isNotEmpty()) {
            val creators = getCreatorsList(colorists)

            binding.clColoristsCover.visibility = View.VISIBLE

            //Mostramos la etiqueta en singular o plurar en función del número de creadores
            binding.tvTextColoristsCover.text = resources.getQuantityString(R.plurals.colorist_cover, colorists.size)
            binding.tvColoristsCover.text = creators
        } else {
            binding.clColoristsCover.visibility = View.GONE
        }
    }

    /**
     * Rotulistas de la portada
     */
    override fun showCoverLetterers(letterers: List<CreatorSummary>) {
        if (letterers.isNotEmpty()) {
            val creators = getCreatorsList(letterers)

            binding.clLetterersCover.visibility = View.VISIBLE

            //Mostramos la etiqueta en singular o plurar en función del número de creadores
            binding.tvTextLetterersCover.text = resources.getQuantityString(R.plurals.letterer_cover, letterers.size)
            binding.tvLetterersCover.text = creators
        } else {
            binding.clLetterersCover.visibility = View.GONE
        }
    }

    /**
     * Pintores de la portada
     */
    override fun showCoverPainters(painters: List<CreatorSummary>) {
        if (painters.isNotEmpty()) {
            val creators = getCreatorsList(painters)

            binding.clPaintersCover.visibility = View.VISIBLE

            //Mostramos la etiqueta en singular o plurar en función del número de creadores
            binding.tvTextPaintersCover.text = resources.getQuantityString(R.plurals.painter_cover, painters.size)
            binding.tvPaintersCover.text = creators
        } else {
            binding.clPaintersCover.visibility = View.GONE
        }
    }

    /**
     * Editores de la portada
     */
    override fun showCoverEditors(editors: List<CreatorSummary>) {
        if (editors.isNotEmpty()) {
            val creators = getCreatorsList(editors)

            binding.clEditorsCover.visibility = View.VISIBLE

            //Mostramos la etiqueta en singular o plurar en función del número de creadores
            binding.tvTextEditorsCover.text = resources.getQuantityString(R.plurals.editor_cover, editors.size)
            binding.tvEditorsCover.text = creators
        } else {
            binding.clEditorsCover.visibility = View.GONE
        }
    }

    /**
     * Indicamos si tenemos o no información de la portada
     */
    override fun showNoCoverInfo(show: Boolean) {
        //En función de si tenemos info de la portada o no, mostramos el "no disponible" o los creadores
        if (show) {
            binding.tvNoCoverInfo.visibility = View.VISIBLE
            binding.llCoverCreators.visibility = View.GONE
        } else {
            binding.tvNoCoverInfo.visibility = View.GONE
            binding.llCoverCreators.visibility = View.VISIBLE
        }
    }

    /**
     * Muestra el número de personajes, eventos e historias en su pestaña correspondiente
     */
    override fun showNumberOfItems(characters: Int, events: Int, stories: Int) {
        binding.tvCharacters.text = getString(R.string.number_of_characters, characters)
        binding.tvEvents.text = getString(R.string.number_of_events, events)
        binding.tvStories.text = getString(R.string.number_of_stories, stories)
    }

    /**
     * Error mostrado cuando el servidor nos devuelve un 404
     */
    override fun onComicNotFound() {
        AppUtils.showDialogInformacion(supportFragmentManager, getString(R.string.error),
            getString(R.string.comic_not_found)) { finish() }
    }

    override fun showCopyright(copyright: String) {
        binding.tvCopyright.text = copyright
    }

    /**
     * Muestra los personajes que salen en el cómic
     */
    override fun showCharacters(characters: CharacterList?) {
        //Seleccionamos la pestaña de los personajes
        binding.tvCharacters.isSelected = true
        binding.tvEvents.isSelected = false
        binding.tvStories.isSelected = false

        if (characters != null) {
            if (characters.items.isNotEmpty()) {
                //Comprobamos si hay más disponibles de los mostrados
                val moreCharactersAvailable = (characters.available ?: 0).minus(characters.returned ?: 0)
                val adapter = CharacterSummaryAdapter(this, characters.items, moreCharactersAvailable, presenter)
                binding.rvItems.adapter = adapter
                binding.rvItems.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            } else {
                showEmptyView(getString(R.string.no_stories_available))
            }
        } else {
            showEmptyView(getString(R.string.no_stories_available))
        }
    }


    /**
     * Historias a las que pertenece
     */
    override fun showStories(stories: StoryList?) {
        //Seleccionamos la pestaña de las histoiras
        binding.tvCharacters.isSelected = false
        binding.tvEvents.isSelected = false
        binding.tvStories.isSelected = true

        if (stories != null) {
            if (stories.items.isNotEmpty()) {
                //Comprobamos si hay más disponibles
                val moreStoriesAvailable = (stories.available ?: 0).minus(stories.returned ?: 0)
                val adapter = StorySummaryAdapter(this, stories.items, moreStoriesAvailable, presenter)
                binding.rvItems.adapter = adapter
                binding.rvItems.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            } else {
                showEmptyView(getString(R.string.no_stories_available))
            }
        } else {
            showEmptyView(getString(R.string.no_stories_available))
        }
    }

    /**
     * Con este método mostramos el emptyView, que es común para todas las pestañas, cuando no tengamos
     * datos en una de las secciones.
     */
    private fun showEmptyView(emptyText: String) {
        binding.rvItems.visibility = View.GONE
        binding.emptyView.visibility = View.VISIBLE
        binding.tvEmptyView.text = emptyText
    }

    /**
     * Eventos en los que está el cómic
     */
    override fun showEvents(events: EventList?) {
        //Seleccionamos la pestaña de los eventos
        binding.tvCharacters.isSelected = false
        binding.tvEvents.isSelected = true
        binding.tvStories.isSelected = false

        //Mostramos la lista de eventos, si tenemos
        if (events != null) {
            if (events.items.isNotEmpty()) {
                //Si el campo "available" es mayor que el "returned", tendremos más eventos disponibles
                val moreEventsAvailable = (events.available ?: 0).minus(events.returned ?: 0)
                val adapter = EventSummaryAdapter(this, events.items, moreEventsAvailable, presenter)
                binding.rvItems.adapter = adapter
                binding.rvItems.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            } else {
                showEmptyView(getString(R.string.no_events_available))
            }
        } else {
            showEmptyView(getString(R.string.no_events_available))
        }
    }

    /**
     * Muestra u oculta la parte de los enlaces disponibles
     */
    override fun showUrls(urls: ArrayList<Url>?) {
        if (urls.isNullOrEmpty()) {
            binding.clEnlaces.visibility = View.GONE
        } else {
            binding.clEnlaces.visibility = View.VISIBLE
            val adapter = UrlAdapter(this, urls, presenter)
            binding.rvUrls.adapter = adapter
        }
    }

    override fun showLoading(show: Boolean) {
        binding.progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Si no hemos podido descargar bien la información del cómic, cerramos la pantalla
     */
    override fun onErrorParsingData(message: String) {
        AppUtils.showDialogInformacion(supportFragmentManager, getString(R.string.error),
            getString(R.string.error_parseando_info, message)) { finish() }
    }

    /**
     * Si no hemos podido obtener el id del comic, cerramos la pantalla
     */
    override fun onComicNotSelectable() {
        AppUtils.showDialogInformacion(supportFragmentManager, getString(R.string.error),
            getString(R.string.error_comic_without_id)) { finish() }
    }
}