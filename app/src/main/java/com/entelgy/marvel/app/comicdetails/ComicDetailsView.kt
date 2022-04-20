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

    fun showPublishedDate(date: Date?)

    fun showAuthors(authors: List<CreatorSummary>)

    fun showPencillers(pencillers: List<CreatorSummary>)

    fun showDescription(description: String?)

    fun showImagesAvailable(show: Boolean = true)

    fun showNumberOfImagesAvailable(number: Int)

    fun showVariantsAvailable(show: Boolean = true)

    fun showNumberOfVariantsAvailable(number: Int)

    fun showSeries(serie: SeriesSummary?)

    fun showCollections(collections: List<ComicSummary>)

    fun showCollectedIssues(collectedIssues: List<ComicSummary>)

    fun showNumber(number: Int?)

    fun showFormat(format: String?)

    fun showPageCount(pageCount: Int?)

    fun showPrice(prices: List<ComicPrice>)

    fun showUPC(upc: String?)

    fun showISBN(isbn: String?)

    fun showEAN(ean: String?)

    fun showISSN(issn: String?)

    fun showDiamondCode(code: String?)

    fun showFOCDate(date: Date?)

    fun showUnlimitedDate(date: Date?)

    fun showDigitalPurchaseDate(date: Date?)

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

    fun showNumberOfItems(characters: Int, events: Int, stories: Int)

    fun onComicNotFound()

    fun showCopyright(copyright: String)

    fun showCharacters(characters: CharacterList?)
    fun showStories(stories: StoryList?)
    fun showEvents(events: EventList?)
    fun showUrls(urls: ArrayList<Url>?)
    fun onErrorParsingData(message: String)
}