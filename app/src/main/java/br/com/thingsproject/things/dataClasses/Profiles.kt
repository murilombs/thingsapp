package br.com.thingsproject.things.dataClasses

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity(tableName = "userProfile")
class Profiles : Parcelable {
    @PrimaryKey
    //var id : Long = 0 PARA TESTAR
    //var id = ""
    var first_name = ""
    var second_name = ""
    var email = ""
    var password = ""
    var profilePicture = ""

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        //dest?.writeString(this.id)
        dest?.writeString(this.first_name)
        dest?.writeString(this.second_name)
        dest?.writeString(this.email)
        dest?.writeString(this.password)

    }

    fun readFromParcel(parcel: Parcel) {
        //this.id = parcel.readString()
        this.first_name = parcel.readString()
        this.second_name = parcel.readString()
        this.email = parcel.readString()
        this.password = parcel.readString()
    }

    companion object {
        private val serialVersionUID = 6601006766832473959L
        @JvmField val CREATOR : Parcelable.Creator<Item> = object : Parcelable.Creator<Item>{
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