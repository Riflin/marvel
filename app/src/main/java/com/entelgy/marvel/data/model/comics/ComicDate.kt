package com.entelgy.marvel.data.model.comics

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

data class ComicDate(
    @SerializedName("type")
    val type: String?,
    @SerializedName("date")
    val date: Date?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        //Dependiendo del byte de control, crearemos una fecha con su timestamp correspondiente o la dejamos a null
        if (parcel.readByte() == 1.toByte())  Date(parcel.readLong()) else null,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        //Escribimos un byte de control para saber si la fecha es nula o no
        if (date != null) {
            parcel.writeByte(1)
            parcel.writeLong(date.time)
        } else {
            parcel.writeByte(0)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComicDate

        if (type != other.type) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type?.hashCode() ?: 0
        result = 31 * result + (date?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "ComicDate(type=$type, date=$date)"
    }

    companion object CREATOR : Parcelable.Creator<ComicDate> {
        override fun createFromParcel(parcel: Parcel): ComicDate {
            return ComicDate(parcel)
        }

        override fun newArray(size: Int): Array<ComicDate?> {
            return arrayOfNulls(size)
        }
    }

}
