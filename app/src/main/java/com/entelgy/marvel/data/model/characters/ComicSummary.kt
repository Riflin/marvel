package com.entelgy.marvel.data.model.characters

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ComicSummary(
    @SerializedName("resourceURI")
    val resourceURI: String?,
    @SerializedName("name")
    val name: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(resourceURI)
        parcel.writeString(name)
    }

    /**
     * Devuelve el id del cómic a partir de su resourceUri
     */
    fun getId(): Int? {
        resourceURI?.let { uri ->
            //El id del comic está en la última parte de la uri (ej: http://gateway.marvel.com/v1/public/comics/4100)
            val urlSplit = uri.split("/")
            return urlSplit[urlSplit.size - 1].toInt()
        } ?: run {
            return null
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "ComicSummary(resourceURI=$resourceURI, name=$name)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComicSummary

        if (resourceURI != other.resourceURI) return false

        return true
    }

    override fun hashCode(): Int {
        return resourceURI?.hashCode() ?: 0
    }

    companion object CREATOR : Parcelable.Creator<ComicSummary> {
        override fun createFromParcel(parcel: Parcel): ComicSummary {
            return ComicSummary(parcel)
        }

        override fun newArray(size: Int): Array<ComicSummary?> {
            return arrayOfNulls(size)
        }
    }

}
