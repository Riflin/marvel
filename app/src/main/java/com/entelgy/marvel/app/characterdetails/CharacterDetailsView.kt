package com.entelgy.marvel.app.characterdetails

import com.entelgy.marvel.app.utils.base.BaseView
import com.entelgy.marvel.data.model.*
import java.util.ArrayList

interface CharacterDetailsView: BaseView {

    fun showImage(path: String)

    fun showName(name: String)

    fun showBio(bio: String?)

    fun showNumberOfItems(comics: Int, series: Int, events: Int, stories: Int)

    fun onDataError()

    fun onCharacterNotFound()

    fun showCopyright(copyright: String)

    fun showComics(comics: ComicList?)
    fun showSeries(series: SeriesList?)
    fun showStories(stories: StoryList?)
    fun showEvents(events: EventList?)
    fun showUrls(urls: ArrayList<Url>?)
    fun openUrl(url: Url)
}