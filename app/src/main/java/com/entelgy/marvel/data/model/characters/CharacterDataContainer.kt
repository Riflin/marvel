package com.entelgy.marvel.data.model.characters

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CharacterDataContainer(
    @SerializedName("offset")
    val offset: Int?,
    @SerializedName("limit")
    val limit: Int?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("results")
    val results: ArrayList<Character>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createTypedArrayList(Character) ?: ArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(offset)
        parcel.writeValue(limit)
        parcel.writeValue(total)
        parcel.writeValue(count)
        parcel.writeTypedList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharacterDataContainer

        if (offset != other.offset) return false
        if (limit != other.limit) return false
        if (total != other.total) return false
        if (count != other.count) return false
        if (results != other.results) return false

        return true
    }

    override fun hashCode(): Int {
        var result = offset ?: 0
        result = 31 * result + (limit ?: 0)
        result = 31 * result + (total ?: 0)
        result = 31 * result + (count ?: 0)
        result = 31 * result + results.hashCode()
        return result
    }

    override fun toString(): String {
        return "CharacterDataContainer(offset=$offset, limit=$limit, total=$total, count=$count, results=$results)"
    }

    companion object CREATOR : Parcelable.Creator<CharacterDataContainer> {
        override fun createFromParcel(parcel: Parcel): CharacterDataContainer {
            return CharacterDataContainer(parcel)
        }

        override fun newArray(size: Int): Array<CharacterDataContainer?> {
            return arrayOfNulls(size)
        }
    }

}
