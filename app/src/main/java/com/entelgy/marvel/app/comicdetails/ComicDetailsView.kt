package com.entelgy.marvel.app.comicdetails

import com.entelgy.marvel.app.utils.base.BaseView
import com.entelgy.marvel.data.model.*
import com.entelgy.marvel.data.model.characters.ComicList
import com.entelgy.marvel.data.model.characters.ComicSummary
import com.entelgy.marvel.data.model.comics.ComicDate
import com.entelgy.marvel.data.model.comics.ComicPrice
import com.entelgy.marvel.data.model.comics.CreatorSummary
import java.util.*

interface ComicDetailsView: BaseView {

    fun showTitle(title: String)

    fun showImage(path: String)

    fun showPublishedDate(date: Date)

    fun showAuthors(vararg author: String)

    fun showPencillers(vararg penciller: String)

    fun showDescription(description: String?)

    fun showImagesAvailable(show: Boolean = true)

    fun showVariantsAvailable(show: Boolean = true)

    fun showCollections(collections: List<ComicSummary>)

    fun showCollectedIssues(collectedIssues: List<ComicSummary>)

    fun showNumber(number: Int?)

    fun showFormat(format: String?)

    fun showPageCount(pageCount: Int)

    fun showPrice(vararg price: ComicPrice)

    fun showUPC(upc: String?)

    fun showISBN(isbn: String?)

    fun showEAN(ean: String?)

    fun showISSN(issn: String?)

    fun showDiamondCode(code: String?)

    fun showDates(dates: List<ComicDate>)

    fun showInkers(inkers: List<CreatorSummary>)

    fun showColorists(colorists: List<CreatorSummary>)

    fun showLetterers(letterers: List<CreatorSummary>)

    fun showPainters(painters: List<CreatorSummary>)

    fun showEditors(editors: List<CreatorSummary>)

    fun showCoverPencilers(pencilers: List<CreatorSummary>)

    fun showCoverInkers(inkers: List<CreatorSummary>)

    fun showCoverColorists(colorists: List<CreatorSummary>)

    fun showCoverLetterers(letterers: List<CreatorSummary>)

    fun showCoverPainters(painters: List<CreatorSummary>)

    fun showCoverEditors(editors: List<CreatorSummary>)

    fun showNoCoverInfo(show: Boolean = true)

    fun showNumberOfItems(characters: Int, series: Int, events: Int, stories: Int)

    fun onComicNotFound()

    fun showCopyright(copyright: String)

    fun showCharacters(characters: CharacterList?)
    fun showSeries(series: SeriesList?)
    fun showStories(stories: StoryList?)
    fun showEvents(events: EventList?)
    fun showUrls(urls: ArrayList<Url>?)
    fun openUrl(url: Url)
}