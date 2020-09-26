package com.sample.myapplication.api.country
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Currency(
    @SerializedName("code")
    val code: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("symbol")
    val symbol: String?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(code)
        writeString(name)
        writeString(symbol)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Currency> = object : Parcelable.Creator<Currency> {
            override fun createFromParcel(source: Parcel): Currency = Currency(source)
            override fun newArray(size: Int): Array<Currency?> = arrayOfNulls(size)
        }
    }
}