package com.example.nimbusweatherapp.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class FavouriteLocation(
    @PrimaryKey
    val locationName : String,
    val latitude : Double,
    val longitude : Double
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(locationName)
        dest.writeDouble(latitude)
        dest.writeDouble(longitude)
    }

    companion object CREATOR : Parcelable.Creator<FavouriteLocation> {
        override fun createFromParcel(parcel: Parcel): FavouriteLocation {
            return FavouriteLocation(parcel)
        }

        override fun newArray(size: Int): Array<FavouriteLocation?> {
            return arrayOfNulls(size)
        }
    }

}
