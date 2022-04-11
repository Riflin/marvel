package com.entelgy.marvel.data.model.comics

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TextObject(
    @SerializedName("type")
    val type: String?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("text")
    val text: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(language)
        parcel.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "TextObject(type=$type, language=$language, text=$text)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextObject

        if (type != other.type) return false
        if (language != other.language) return false
        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type?.hashCode() ?: 0
        result = 31 * result + (language?.hashCode() ?: 0)
        result = 31 * result + (text?.hashCode() ?: 0)
        return result
    }

    companion object CREATOR : Parcelable.Creator<TextObject> {
        override fun createFromParcel(parcel: Parcel): TextObject {
            return TextObject(parcel)
        }

        override fun newArray(size: Int): Array<TextObject?> {
            return arrayOfNulls(size)
        }
    }

}
