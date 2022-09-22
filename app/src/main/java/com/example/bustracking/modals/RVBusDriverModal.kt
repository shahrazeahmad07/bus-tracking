package com.example.bustracking.modals

import android.os.Parcel
import android.os.Parcelable

class RVBusDriverModal() : Parcelable {
    var userName: String? = null
    var longitude: Double? = null
    var latitude: Double? = null

    constructor(parcel: Parcel) : this() {
        userName = parcel.readString()
        longitude = parcel.readValue(Double::class.java.classLoader) as? Double
        latitude = parcel.readValue(Double::class.java.classLoader) as? Double
    }

    constructor(userName: String, longitude: Double, latitude: Double) : this() {
        this.userName = userName
        this.longitude = longitude
        this.latitude = latitude
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userName)
        parcel.writeValue(longitude)
        parcel.writeValue(latitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RVBusDriverModal> {
        override fun createFromParcel(parcel: Parcel): RVBusDriverModal {
            return RVBusDriverModal(parcel)
        }

        override fun newArray(size: Int): Array<RVBusDriverModal?> {
            return arrayOfNulls(size)
        }
    }
}