package br.com.thingsproject.things.domain

import android.util.Base64
import br.com.thingsproject.things.dataClasses.Item
import java.io.File
import br.com.thingsproject.things.extensions.fromJson
import br.com.thingsproject.things.extensions.toJson
import br.com.thingsproject.things.utils.HttpHelper
import br.com.thingsproject.things.utils.Prefs

//WEB SERVICE
object ItensService {
    private val Base_URL = "http://thingsproject.com.br/itens/"

    fun getItens(): List<Item> {
        val url = Base_URL
        val json = HttpHelper.get(url)
        val itens = fromJson<List<Item>>(json)
        return itens
    }

    fun save(item: Item): Response {
        val t = Prefs.getString("token")
        val json = HttpHelper.postU(Base_URL, t ,item.toJson())
        val response = fromJson<Response>(json)
        return response
    }
    /*
    fun delete(item: Item): Response {
        val url = "$Base_URL/${item.id}"
        val json = HttpHelper.delete(url)
        val response = fromJson<Response>(json)
        return response
    }
    */

    fun postFoto(file: File): String{
        // Converte para Base64
        val bytes = file.readBytes()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}