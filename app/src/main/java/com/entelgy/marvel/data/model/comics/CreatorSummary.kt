package com.entelgy.marvel.data.model.comics

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CreatorSummary(
    @SerializedName("resourceURI")
    val resourceURI: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("role")
    val role: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(resourceURI)
        parcel.writeString(name)
        parcel.writeString(role)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CreatorSummary

        if (resourceURI != other.resourceURI) return false

        return true
    }

    override fun hashCode(): Int {
        return resourceURI?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "CreatorSummary(resourceURI=$resourceURI, name=$name, role=$role)"
    }

    companion object CREATOR : Parcelable.Creator<CreatorSummary> {
        override fun createFromParcel(parcel: Parcel): CreatorSummary {
            return CreatorSummary(parcel)
        }

        override fun newArray(size: Int): Array<CreatorSummary?> {
            return arrayOfNulls(size)
        }
    }

}
