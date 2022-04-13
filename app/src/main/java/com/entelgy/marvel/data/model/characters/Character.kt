package com.entelgy.marvel.data.model.characters

import android.os.Parcel
import android.os.Parcelable
import com.entelgy.marvel.data.model.*
import com.entelgy.marvel.data.model.imageformats.ImageFormat
import com.entelgy.marvel.data.model.imageformats.PortraitImage
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class Character(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("modified")
    val modified: Date?,
    @SerializedName("resourceURI")
    val resourceURI: String?,
    @SerializedName("urls")
    val urls: ArrayList<Url>?,
    @SerializedName("thumbnail")
    val thumbnail: Image?,
    @SerializedName("comics")
    val commics: ComicList?,
    @SerializedName("stories")
    val stories: StoryList?,
    @SerializedName("events")
    val events: EventList?,
    @SerializedName("series")
    val series: SeriesList?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        //Dependiendo del byte de control, crearemos una fecha con su timestamp correspondiente o la dejamos a null
        if (parcel.readByte() == 1.toByte())  Date(parcel.readLong()) else null,
        parcel.readString(),
        parcel.createTypedArrayList(Url),
        parcel.readParcelable(Image::class.java.classLoader),
        parcel.readParcelable(ComicList::class.java.classLoader),
        parcel.readParcelable(StoryList::class.java.classLoader),
        parcel.readParcelable(EventList::class.java.classLoader),
        parcel.readParcelable(SeriesList::class.java.classLoader)
    )

    fun getThumbnailPath(imageFormat: ImageFormat): String {
        return thumbnail?.path + "/" + PortraitImage.Xlarge.format() + "." + thumbnail?.extension
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(description)
        //Escribimos un byte de control para saber si la fecha es nula o no
        if (modified != null) {
            parcel.writeByte(1)
            parcel.writeLong(modified.time)
        } else {
            parcel.writeByte(0)
        }
        parcel.writeString(resourceURI)
        parcel.writeTypedList(urls)
        parcel.writeParcelable(thumbnail, flags)
        parcel.writeParcelable(commics, flags)
        parcel.writeParcelable(stories, flags)
        parcel.writeParcelable(events, flags)
        parcel.writeParcelable(series, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Character> {
        override fun createFromParcel(parcel: Parcel): Character {
            return Character(parcel)
        }

        override fun newArray(size: Int): Array<Character?> {
            return arrayOfNulls(size)
        }
    }

}
