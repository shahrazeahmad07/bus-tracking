package com.example.bustracking.modals

import android.os.Parcel
import android.os.Parcelable

class ComplainModal(): Parcelable {
    var author: String? = null
    var complain: String? = null

    constructor(parcel: Parcel) : this() {
        author = parcel.readString()
        complain = parcel.readString()
    }

    constructor(author: String, complain: String) : this() {
        this.author = author
        this.complain = complain
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(author)
        parcel.writeString(complain)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ComplainModal> {
        override fun createFromParcel(parcel: Parcel): ComplainModal {
            return ComplainModal(parcel)
        }

        override fun newArray(size: Int): Array<ComplainModal?> {
            return arrayOfNulls(size)
        }
    }
}