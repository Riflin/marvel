package com.entelgy.marvel.data.model.comics

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes
import com.entelgy.marvel.R
import com.google.gson.annotations.SerializedName

data class ComicPrice(
    @SerializedName("type")
    val type: String?,
    @SerializedName("price")
    val price: Float?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeValue(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComicPrice

        if (type != other.type) return false
        if (price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type?.hashCode() ?: 0
        result = 31 * result + (price?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "ComicPrice(type=$type, price=$price)"
    }

    companion object CREATOR : Parcelable.Creator<ComicPrice> {
        override fun createFromParcel(parcel: Parcel): ComicPrice {
            return ComicPrice(parcel)
        }

        override fun newArray(size: Int): Array<ComicPrice?> {
            return arrayOfNulls(size)
        }
    }

}

enum class PriceType(val tipo: String, @StringRes val text: Int) {
    Impreso("printPrice", R.string.precio_impreso), Digital("digitalPurchasePrice", R.string.precio_digital)

}
