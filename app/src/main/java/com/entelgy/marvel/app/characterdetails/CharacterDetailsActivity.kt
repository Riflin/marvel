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

        fun createNewIntent(context: Context, characterId: Int): Intent {
            return Intent(context, CharacterDetailsActivity::class.java).apply {
                putExtra(Constants.CHARACTER_ID, characterId)
            }
        }

        fun createNewIntent(context: Context, character: Character): Intent {
            return Intent(context, CharacterDetailsActivity::class.java).apply {
                putExtra(Constants.CHARACTER_ID, character.id)
                putExtra(Constants.CHARACTER, character)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCharacterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        presenter.getData(intent)
    }

    override fun init() {
        presenter = PresenterFactory.getCharacterDetailsPresenter()
        presenter.view = this
        presenter.create()
    }

    override fun initViews() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    override fun showImage(path: String) {
        binding.progressImage.visibility = View.VISIBLE
        Picasso.get().load(path).placeholder(R.drawable.ic_broken_image).into(binding.ivProfile, object: Callback {
            override fun onSuccess() {
                binding.progressImage.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                binding.progressImage.visibility = View.GONE
            }
        })
    }

    override fun showName(name: String) {
        binding.tvTitle.text = name
    }

    override fun showBio(bio: String?) {
        binding.tvDescription.text = if (bio.isNullOrBlank()) getString(R.string.no_description_available) else bio
    }

    override fun showNumberOfItems(comics: Int, series: Int, events: Int, stories: Int) {
        binding.tvComics.text = getString(R.string.number_of_comics, comics)
        binding.tvSeries.text = getString(R.string.number_of_series, series)
        binding.tvEvents.text = getString(R.string.number_of_events, events)
        binding.tvStories.text = getString(R.string.number_of_stories, stories)
    }

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

    override fun showComics(comics: ComicList?) {
        binding.tvComics.isSelected = true
        binding.tvEvents.isSelected = false
        binding.tvStories.isSelected = false
        binding.tvSeries.isSelected = false

        if (comics != null) {
            if (comics.items.isNotEmpty()) {
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

    override fun showSeries(series: SeriesList?) {
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

    private fun showEmptyView(emptyText: String) {
        binding.rvItems.visibility = View.GONE
        binding.emptyView.visibility = View.VISIBLE
        binding.tvEmptyView.text = emptyText
    }

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