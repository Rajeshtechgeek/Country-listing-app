package com.sample.myapplication.api.response
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Language(
    @SerializedName("iso639_1")
    val iso6391: String?,
    @SerializedName("iso639_2")
    val iso6392: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("nativeName")
    val nativeName: String?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(iso6391)
        writeString(iso6392)
        writeString(name)
        writeString(nativeName)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Language> = object : Parcelable.Creator<Language> {
            override fun createFromParcel(source: Parcel): Language = Language(source)
            override fun newArray(size: Int): Array<Language?> = arrayOfNulls(size)
        }
    }
}