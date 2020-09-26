package com.sample.myapplication.api.country

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CountryResponseItem(
    @SerializedName("alpha2Code")
    val alpha2Code: String?,
    @SerializedName("alpha3Code")
    val alpha3Code: String?,
    @SerializedName("altSpellings")
    val altSpellings: List<String>?,
    @SerializedName("area")
    val area: Double?,
    @SerializedName("borders")
    val borders: List<String>?,
    @SerializedName("callingCodes")
    val callingCodes: List<String>?,
    @SerializedName("capital")
    val capital: String?,
    @SerializedName("cioc")
    val cioc: String?,
    @SerializedName("currencies")
    val currencies: List<Currency>?,
    @SerializedName("demonym")
    val demonym: String?,
    @SerializedName("flag")
    val flag: String?,
    @SerializedName("gini")
    val gini: Double?,
    @SerializedName("languages")
    val languages: List<Language>?,
    @SerializedName("latlng")
    val latlng: List<Double>?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("nativeName")
    val nativeName: String?,
    @SerializedName("numericCode")
    val numericCode: String?,
    @SerializedName("population")
    val population: Int?,
    @SerializedName("region")
    val region: String?,
    @SerializedName("regionalBlocs")
    val regionalBlocs: List<RegionalBloc>?,
    @SerializedName("subregion")
    val subregion: String?,
    @SerializedName("timezones")
    val timezones: List<String>?,
    @SerializedName("topLevelDomain")
    val topLevelDomain: List<String>?,
    @SerializedName("translations")
    val translations: Translations?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Currency.CREATOR),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.createTypedArrayList(Language.CREATOR),
        parcel.createDoubleArray()?.toList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.createTypedArrayList(RegionalBloc.CREATOR),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.readParcelable(Translations::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(alpha2Code)
        parcel.writeString(alpha3Code)
        parcel.writeStringList(altSpellings)
        parcel.writeValue(area)
        parcel.writeStringList(borders)
        parcel.writeStringList(callingCodes)
        parcel.writeString(capital)
        parcel.writeString(cioc)
        parcel.writeTypedList(currencies)
        parcel.writeString(demonym)
        parcel.writeString(flag)
        parcel.writeValue(gini)
        parcel.writeTypedList(languages)
        parcel.writeDoubleArray(latlng?.toDoubleArray())
        parcel.writeString(name)
        parcel.writeString(nativeName)
        parcel.writeString(numericCode)
        parcel.writeValue(population)
        parcel.writeString(region)
        parcel.writeTypedList(regionalBlocs)
        parcel.writeString(subregion)
        parcel.writeStringList(timezones)
        parcel.writeStringList(topLevelDomain)
        parcel.writeParcelable(translations, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CountryResponseItem> {
        override fun createFromParcel(parcel: Parcel): CountryResponseItem {
            return CountryResponseItem(parcel)
        }

        override fun newArray(size: Int): Array<CountryResponseItem?> {
            return arrayOfNulls(size)
        }
    }
}