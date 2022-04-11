package com.entelgy.marvel.data.model.comics

import com.google.gson.annotations.SerializedName

data class ComicDataWrapper(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("copyright")
    val copyright: String?,
    @SerializedName("attributionText")
    val attributionText: String?,
    @SerializedName("attributionHTML")
    val attributionHTML: String?,
    @SerializedName("etag")
    val etag: String?,
    @SerializedName("data")
    val data: ComicDataContainer?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComicDataWrapper

        if (etag != other.etag) return false

        return true
    }

    override fun hashCode(): Int {
        return etag?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "ComicDataWrapper(code=$code, status=$status, copyright=$copyright, attributionText=$attributionText, attributionHTML=$attributionHTML, etag=$etag, data=$data)"
    }
}