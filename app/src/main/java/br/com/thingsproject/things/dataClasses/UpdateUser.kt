package br.com.thingsproject.things.dataClasses

import android.os.Parcel
import android.os.Parcelable

class UpdateUser: Parcelable {
    var first_name = ""
    var second_name = ""
    var profilePicture = ""

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.first_name)
        dest?.writeString(this.second_name)

    }

    fun readFromParcel(parcel: Parcel) {
        this.first_name = parcel.readString()
        this.second_name = parcel.readString()
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