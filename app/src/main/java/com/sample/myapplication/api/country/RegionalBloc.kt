package com.sample.myapplication.api.country

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RegionalBloc(
    @SerializedName("acronym")
    val acronym: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("otherAcronyms")
    val otherAcronyms: List<Any>?,
    @SerializedName("otherNames")
    val otherNames: List<Any>?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        ArrayList<Any>().apply { source.readList(this as List<*>, Any::class.java.classLoader) },
        ArrayList<Any>().apply { source.readList(this as List<*>, Any::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(acronym)
        writeString(name)
        writeList(otherAcronyms)
        writeList(otherNames)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RegionalBloc> = object : Parcelable.Creator<RegionalBloc> {
            override fun createFromParcel(source: Parcel): RegionalBloc = RegionalBloc(source)
            override fun newArray(size: Int): Array<RegionalBloc?> = arrayOfNulls(size)
        }
    }
}