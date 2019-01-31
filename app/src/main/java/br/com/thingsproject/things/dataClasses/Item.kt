package br.com.thingsproject.things.dataClasses

import android.os.Parcel
import android.os.Parcelable

class Item : Parcelable {
    //nome do item
        var name = ""
    //entrega
        var availability : Boolean = true
    //imagens
        var itensImages = ""
    //tempo e custo
        var timeCust = ""
    //descricao
        var description = ""
    //localização
        var latitude : String= ""
            get() = if (field.trim().isEmpty()) "0.0" else field
        var longitude = ""
            get() = if (field.trim().isEmpty()) "0.0" else field
    //diponibilidade para entrega
        var delivery = ""
    //id do dono
        var owner = ""

    override fun toString(): String {
                return "Item{nome=$name}"
        }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.name)
        dest?.writeString(this.delivery)
        dest?.writeString(this.itensImages)
        dest?.writeString(this.timeCust)
        dest?.writeString(this.description)
        dest?.writeString(this.latitude)
        dest?.writeString(this.longitude)
        dest?.writeString(this.owner)
    }

    fun readFromParcel(parcel: Parcel) {
        this.name = parcel.readString()
        this.delivery = parcel.readString()
        this.itensImages = parcel.readString()
        this.timeCust = parcel.readString()
        this.description = parcel.readString()
        this.latitude = parcel.readString()
        this.longitude = parcel.readString()
        this.owner = parcel.readString()
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