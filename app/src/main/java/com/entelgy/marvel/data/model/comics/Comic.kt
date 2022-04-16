package com.entelgy.marvel.data.model.comics

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.entelgy.marvel.R
import com.entelgy.marvel.data.model.*
import com.entelgy.marvel.data.utils.Constants
import com.google.gson.annotations.SerializedName
import java.util.*

data class Comic(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("digitalId")
    val digitalId: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("issueNumber")
    val issueNumber: Double?,
    @SerializedName("variantDescription")
    val variantDescription: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("modified")
    val modified: Date?,
    @SerializedName("isbn")
    val isbn: String?,
    @SerializedName("upc")
    val upc: String?,
    @SerializedName("diamondCode")
    val diamondCode: String?,
    @SerializedName("ean")
    val ean: String?,
    @SerializedName("issn")
    val issn: String?,
    @SerializedName("format")
    val format: String?,
    @SerializedName("pageCount")
    val pageCount: Int?,
    @SerializedName("textObjects")
    val textObjects: ArrayList<TextObject>,
    @SerializedName("resourceURI")
    val resourceURI: String?,
    @SerializedName("urls")
    val urls: ArrayList<Url>?,
    @SerializedName("series")
    val series: SeriesSummary?,
    @SerializedName("variants")
    val variants: ArrayList<ComicSummary>,
    @SerializedName("collections")
    val collections: ArrayList<ComicSummary>,
    @SerializedName("collectedIssues")
    val collectedIssues: ArrayList<ComicSummary>,
    @SerializedName("dates")
    val dates: ArrayList<ComicDate>,
    @SerializedName("prices")
    val prices: ArrayList<ComicPrice>,
    @SerializedName("thumbnail")
    val thumbnail: Image?,
    @SerializedName("images")
    val images: ArrayList<Image>,
    @SerializedName("creators")
    val creators: CreatorList?,
    @SerializedName("characters")
    val characters: CharacterList?,
    @SerializedName("stories")
    val stories: StoryList?,
    @SerializedName("events")
    val events: EventList?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        //Dependiendo del byte de control, crearemos una fecha con su timestamp correspondiente o la dejamos a null
        if (parcel.readByte() == 1.toByte())  Date(parcel.readLong()) else null,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createTypedArrayList(TextObject.CREATOR) ?: ArrayList(),
        parcel.readString(),
        parcel.createTypedArrayList(Url.CREATOR) ?: ArrayList(),
        parcel.readParcelable(SeriesSummary::class.java.classLoader),
        parcel.createTypedArrayList(ComicSummary.CREATOR) ?: ArrayList(),
        parcel.createTypedArrayList(ComicSummary.CREATOR) ?: ArrayList(),
        parcel.createTypedArrayList(ComicSummary.CREATOR) ?: ArrayList(),
        parcel.createTypedArrayList(ComicDate.CREATOR) ?: ArrayList(),
        parcel.createTypedArrayList(ComicPrice.CREATOR) ?: ArrayList(),
        parcel.readParcelable(Image::class.java.classLoader),
        parcel.createTypedArrayList(Image.CREATOR) ?: ArrayList(),
        parcel.readParcelable(CreatorList::class.java.classLoader),
        parcel.readParcelable(CharacterList::class.java.classLoader),
        parcel.readParcelable(StoryList::class.java.classLoader),
        parcel.readParcelable(EventList::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(digitalId)
        parcel.writeString(title)
        parcel.writeValue(issueNumber)
        parcel.writeString(variantDescription)
        parcel.writeString(description)
        //Escribimos un byte de control para saber si la fecha es nula o no
        if (modified != null) {
            parcel.writeByte(1)
            parcel.writeLong(modified.time)
        } else {
            parcel.writeByte(0)
        }
        parcel.writeString(isbn)
        parcel.writeString(upc)
        parcel.writeString(diamondCode)
        parcel.writeString(ean)
        parcel.writeString(issn)
        parcel.writeString(format)
        parcel.writeValue(pageCount)
        parcel.writeTypedList(textObjects)
        parcel.writeString(resourceURI)
        parcel.writeTypedList(urls)
        parcel.writeParcelable(series, flags)
        parcel.writeTypedList(variants)
        parcel.writeTypedList(collections)
        parcel.writeTypedList(collectedIssues)
        parcel.writeTypedList(dates)
        parcel.writeTypedList(prices)
        parcel.writeParcelable(thumbnail, flags)
        parcel.writeTypedList(images)
        parcel.writeParcelable(creators, flags)
        parcel.writeParcelable(characters, flags)
        parcel.writeParcelable(stories, flags)
        parcel.writeParcelable(events, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Comic

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id ?: 0
    }

    override fun toString(): String {
        return "Comic(id=$id, digitalId=$digitalId, title=$title, issueNumber=$issueNumber, variantDescription=$variantDescription, description=$description, modified=$modified, isbn=$isbn, upc=$upc, diamondCode=$diamondCode, ean=$ean, issn=$issn, format=$format, pageCount=$pageCount, textObjects=$textObjects, resourceURI=$resourceURI, urls=$urls, series=$series, variants=$variants, collections=$collections, collectedIssues=$collectedIssues, dates=$dates, prices=$prices, thumbnail=$thumbnail, images=$images, creators=$creators, characters=$characters, stories=$stories, events=$events)"
    }

    fun getRole(context: Context, role: String, quantity: Int): String {
        return when (role) {
            Constants.WRITER -> context.resources.getQuantityString(R.plurals.writer, quantity)
            Constants.PENCILLER -> context.resources.getQuantityString(R.plurals.penciller, quantity)
            Constants.PENCILLER_COVER -> context.resources.getQuantityString(R.plurals.penciller_cover, quantity)
            Constants.LETTERER -> context.resources.getQuantityString(R.plurals.letterer, quantity)
            Constants.COLORIST -> context.resources.getQuantityString(R.plurals.colorist, quantity)
            Constants.EDITOR -> context.resources.getQuantityString(R.plurals.editor, quantity)
            Constants.INKER -> context.resources.getQuantityString(R.plurals.inker, quantity)
            else -> role
        }
    }

    companion object CREATOR : Parcelable.Creator<Comic> {
        override fun createFromParcel(parcel: Parcel): Comic {
            return Comic(parcel)
        }

        override fun newArray(size: Int): Array<Comic?> {
            return arrayOfNulls(size)
        }
    }

}
