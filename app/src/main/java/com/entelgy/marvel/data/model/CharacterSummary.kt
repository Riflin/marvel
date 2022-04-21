package com.entelgy.marvel.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.lang.Exception

data class CharacterSummary(
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

    //Obtiene el id del personaje a partir de su resourceUri
    fun getId(): Int? {
        resourceURI?.let { uri ->
            val urlSplit = uri.split("/")
            try {
                return urlSplit[urlSplit.size - 1].toInt()
            } catch (_: Exception) {
                return null
            }
        } ?: run {
            return null
        }
    }

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

        other as CharacterSummary

        if (resourceURI != other.resourceURI) return false

        return true
    }

    override fun hashCode(): Int {
        return resourceURI?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "CharacterSummary(resourceURI=$resourceURI, name=$name, role=$role)"
    }

    companion object CREATOR : Parcelable.Creator<CharacterSummary> {
        override fun createFromParcel(parcel: Parcel): CharacterSummary {
            return CharacterSummary(parcel)
        }

        override fun newArray(size: Int): Array<CharacterSummary?> {
            return arrayOfNulls(size)
        }
    }

}
