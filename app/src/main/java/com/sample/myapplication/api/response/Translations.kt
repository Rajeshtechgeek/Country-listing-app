package com.sample.myapplication.api.response
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Translations(
    @SerializedName("br")
    val br: String?,
    @SerializedName("de")
    val de: String?,
    @SerializedName("es")
    val es: String?,
    @SerializedName("fa")
    val fa: String?,
    @SerializedName("fr")
    val fr: String?,
    @SerializedName("hr")
    val hr: String?,
    @SerializedName("it")
    val `it`: String?,
    @SerializedName("ja")
    val ja: String?,
    @SerializedName("nl")
    val nl: String?,
    @SerializedName("pt")
    val pt: String?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(br)
        writeString(de)
        writeString(es)
        writeString(fa)
        writeString(fr)
        writeString(hr)
        writeString(it)
        writeString(ja)
        writeString(nl)
        writeString(pt)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Translations> = object : Parcelable.Creator<Translations> {
            override fun createFromParcel(source: Parcel): Translations = Translations(source)
            override fun newArray(size: Int): Array<Translations?> = arrayOfNulls(size)
        }
    }
}