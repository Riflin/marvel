package com.entelgy.marvel.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SeriesList(
    @SerializedName("available")
    val available: Int?,
    @SerializedName("returned")
    val returned: Int?,
    @SerializedName("collectionURI")
    val collectionURI: String?,
    @SerializedName("items")
    val items: ArrayList<SeriesSummary>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.createTypedArrayList(SeriesSummary.CREATOR) ?: ArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(available)
        parcel.writeValue(returned)
        parcel.writeString(collectionURI)
        parcel.writeTypedList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SeriesList

        if (collectionURI != other.collectionURI) return false

        return true
    }

    override fun hashCode(): Int {
        return collectionURI?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "SeriesList(available=$available, returned=$returned, collectionURI=$collectionURI, items=$items)"
    }

    companion object CREATOR : Parcelable.Creator<SeriesList> {
        override fun createFromParcel(parcel: Parcel): SeriesList {
            return SeriesList(parcel)
        }

        override fun newArray(size: Int): Array<SeriesList?> {
            return arrayOfNulls(size)
        }
    }

}
