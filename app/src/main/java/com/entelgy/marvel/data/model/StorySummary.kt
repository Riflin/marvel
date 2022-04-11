package com.entelgy.marvel.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class StorySummary(
    @SerializedName("resourceURI")
    val resourceURI: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(resourceURI)
        parcel.writeString(name)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "StorySummary(resourceURI=$resourceURI, name=$name, type=$type)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StorySummary

        if (resourceURI != other.resourceURI) return false

        return true
    }

    override fun hashCode(): Int {
        return resourceURI?.hashCode() ?: 0
    }

    companion object CREATOR : Parcelable.Creator<StorySummary> {
        override fun createFromParcel(parcel: Parcel): StorySummary {
            return StorySummary(parcel)
        }

        override fun newArray(size: Int): Array<StorySummary?> {
            return arrayOfNulls(size)
        }
    }

}
