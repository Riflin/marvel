package com.entelgy.marvel.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SeriesSummary(
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

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "SeriesSummary(resourceURI=$resourceURI, name=$name)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SeriesSummary

        if (resourceURI != other.resourceURI) return false

        return true
    }

    override fun hashCode(): Int {
        return resourceURI?.hashCode() ?: 0
    }

    companion object CREATOR : Parcelable.Creator<SeriesSummary> {
        override fun createFromParcel(parcel: Parcel): SeriesSummary {
            return SeriesSummary(parcel)
        }

        override fun newArray(size: Int): Array<SeriesSummary?> {
            return arrayOfNulls(size)
        }
    }

}
