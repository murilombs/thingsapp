package br.com.thingsproject.things.dataClasses

import android.os.Parcel
import android.os.Parcelable

class Autentifica: Parcelable {
    var email = ""
    var password = ""

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.email)
        dest?.writeString(this.password)

    }

    fun readFromParcel(parcel: Parcel) {
        this.email = parcel.readString()
        this.password = parcel.readString()
    }

    companion object {
        private val serialVersionUID = 6601006766832473959L
        @JvmField val CREATOR : Parcelable.Creator<Item> = object : Parcelable.Creator<Item> {
            override fun createFromParcel(parcel: Parcel): Item {
                val i = Item()
                i.readFromParcel(parcel)
                return i
            }

            override fun newArray(size: Int): Array<Item?> {
                return arrayOfNulls(size)
            }
        }
    }
}