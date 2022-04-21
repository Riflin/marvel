package com.entelgy.marvel.app.characterdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.entelgy.marvel.R
import com.entelgy.marvel.app.characterdetails.adapter.*
import com.entelgy.marvel.app.characterdetails.presenter.CharacterDetailsPresenter
import com.entelgy.marvel.app.presenter.PresenterFactory
import com.entelgy.marvel.app.utils.AppUtils
import com.entelgy.marvel.app.utils.base.BaseActivity
import com.entelgy.marvel.data.model.EventList
import com.entelgy.marvel.data.model.SeriesList
import com.entelgy.marvel.data.model.StoryList
import com.entelgy.marvel.data.model.Url
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.data.model.characters.ComicList
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.databinding.ActivityCharacterDetailsBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CharacterDetailsActivity : BaseActivity(), CharacterDetailsView {

    private lateinit var binding: ActivityCharacterDetailsBinding

    private lateinit var presenter: CharacterDetailsPresenter

    companion object {

        /**
         * Abrimos la activity con el id y nombre del personaje, para descargarnos la información
         * desde el servidor
         */
        fun createNewIntent(context: Context, characterId: Int, characterName: String): Intent {
            return Intent(context, CharacterDetailsActivity::class.java).apply {
                putExtra(Constants.CHARACTER_ID, characterId)
                putExtra(Constants.CHARACTER_NAME, characterName)
            }
        }

        /**
         * Abrimos la activity con los datos completos del personaje, por lo que podemos mostrarlos
         * sin necesidad de llamar a la api
         */
        fun createNewIntent(context: Context, character: Character): Intent {
            return Intent(context, CharacterDetailsActivity::class.java).apply {
                putExtra(Constants.CHARACTER_ID, character.id)
                putExtra(Constants.CHARACTER, character)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inflamos la vista
        binding = ActivityCharacterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Comprobamos los datos que nos llegan en el intent
        presenter.getData(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun init() {
        //Inicializamos el presenter
        presenter = PresenterFactory.getCharacterDetailsPresenter()
        presenter.view = this
        presenter.create()
    }

    override fun initViews() {
        //Inicializamos la toolbar para habilitar la flechita hacia atrás
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //Los layoutManager para mostrar los items (series, comics, etc) del personaje y las urls
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvItems.layoutManager = layoutManager

        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvUrls.layoutManager = layoutManager2
    }

    override fun attachListenersToTheViews() {
        binding.tvComics.setOnClickListener { presenter.showComics() }
        binding.tvEvents.setOnClickListener { presenter.showEvents() }
        binding.tvSeries.setOnClickListener { presenter.showSeries() }
        binding.tvStories.setOnClickListener { presenter.showStories() }
        binding.ivProfile.setOnClickListener { presenter.showPhotoDetail() }
    }

    /**
     * Mostramos la imagen del personaje
     */
    override fun showImage(path: String) {
        binding.progressImage.visibility = View.VISIBLE
        Picasso.get().load(path).into(binding.ivProfile, object: Callback {
            override fun onSuccess() {
                binding.progressImage.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                //Si falla, imagen indicándolo
                binding.progressImage.visibility = View.GONE
                binding.ivProfile.setImageResource(R.drawable.ic_broken_image)
            }
        })
    }

    /**
     * Nombre del personaje
     */
    override fun showName(name: String) {
        supportActionBar?.title = name
    }

    /**
     * Descripción del personaje
     */
    override fun showBio(bio: String?) {
        binding.tvDescription.text = if (bio.isNullOrBlank()) getString(R.string.no_description_available) else bio
    }

    /**
     * Número de cómics, series, eventos e historias en los que aparece el personaje
     */
    override fun showNumberOfItems(comics: Int, series: Int, events: Int, stories: Int) {
        binding.tvComics.text = getString(R.string.number_of_comics, comics)
        binding.tvSeries.text = getString(R.string.number_of_series, series)
        binding.tvEvents.text = getString(R.string.number_of_events, events)
        binding.tvStories.text = getString(R.string.number_of_stories, stories)
    }

    /**
     * Si no hemos encontrado el personaje indicado, error y cerramos activity
     */
    override fun onCharacterNotFound() {
        AppUtils.showDialogInformacion(supportFragmentManager, getString(R.string.error),
            getString(R.string.character_not_found)) { finish() }
    }

    override fun showLoading(show: Boolean) {
        binding.progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showCopyright(copyright: String) {
        binding.tvCopyright.text = copyright
    }

    /**
     * Mostramos los comics en los que aparece el personaje
     */
    override fun showComics(comics: ComicList?) {
        //Seleccionamos la pestaña de los comics
        binding.tvComics.isSelected = true
        binding.tvEvents.isSelected = false
        binding.tvStories.isSelected = false
        binding.tvSeries.isSelected = false

        //Mostramos los comics en el adapter o el emptyView si no tenemos
        if (comics != null) {
            if (comics.items.isNotEmpty()) {
                //Comprobamos si hay más cómics de los que tenemos para mostrar
                val moreComicsAvailable = (comics.available ?: 0).minus(comics.returned ?: 0)
                val adapter = ComicSummaryAdapter(this, comics.items, moreComicsAvailable, presenter)
                binding.rvItems.adapter = adapter
                binding.rvItems.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            } else {
                showEmptyView(getString(R.string.no_comics_available))
            }
        } else {
            showEmptyView(getString(R.string.no_comics_available))
        }
    }

    /**
     * Mostramos las series del personaje
     */
    override fun showSeries(series: SeriesList?) {
        //Seleccionamos su pestaña correspondiente
        binding.tvComics.isSelected = false
        binding.tvEvents.isSelected = false
        binding.tvStories.isSelected = false
        binding.tvSeries.isSelected = true

        if (series != null) {
            if (series.items.isNotEmpty()) {
                val moreSeriesAvailable = (series.available ?: 0).minus(series.returned ?: 0)
                val adapter = SeriesSummaryAdapter(this, series.items, moreSeriesAvailable, presenter)
                binding.rvItems.adapter = adapter
                binding.rvItems.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            } else {
                showEmptyView(getString(R.string.no_series_available))
            }
        } else {
            showEmptyView(getString(R.string.no_series_available))
        }
    }

    /**
     * Historias del personaje
     */
    override fun showStories(stories: StoryList?) {
        binding.tvComics.isSelected = false
        binding.tvEvents.isSelected = false
        binding.tvStories.isSelected = true
        binding.tvSeries.isSelected = false

        if (stories != null) {
            if (stories.items.isNotEmpty()) {
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
     * Muestra los eventos donde aparece el personaje
     */
    override fun showEvents(events: EventList?) {
        binding.tvComics.isSelected = false
        binding.tvEvents.isSelected = true
        binding.tvStories.isSelected = false
        binding.tvSeries.isSelected = false

        if (events != null) {
            if (events.items.isNotEmpty()) {
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
     * Muestra un emptyView con un texto indicando que no tenemos comics/eventos/historias/series disponibles
     */
    private fun showEmptyView(emptyText: String) {
        binding.rvItems.visibility = View.GONE
        binding.emptyView.visibility = View.VISIBLE
        binding.tvEmptyView.text = emptyText
    }

    /**
     * Muestra los enlaces en una lista, o esconde el layout si no tenemos
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
}