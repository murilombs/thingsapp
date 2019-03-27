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

    // fazer um get que busque itens pelo ID proprietario
    fun getItensIDUser(id: String): List<Item> {
        val url = "http://thingsproject.com.br/itens/owner/${id}"
        val json = HttpHelper.getItensUserID(url)
        val itens = fromJson<List<Item>>(json)
        return itens
    }

    // fazer um get que busque itens pelo ID proprietario
    fun getItensByName(name: String?): List<Item> {
        val url = "http://thingsproject.com.br/itens/name/${name}"
        val json = HttpHelper.getItensUserID(url)
        val itens = fromJson<List<Item>>(json)
        return itens
    }

    fun save(item: Item): Response {
        val t = Prefs.getString("token")
        val json = HttpHelper.postU(Base_URL, t ,item.toJson())
        val response = fromJson<Response>(json)
        return response
    }

    // PUT dos itens
    fun putItens(id: String, item: Item, token : String): Response {
        val url = "${Base_URL}$id"
        val json = HttpHelper.put(url, token, item.toJson())
        val response = fromJson<Response>(json)
        return response
    }

    fun delete(id: String, token: String): Response {
        val url = "$Base_URL/$id"
        val json = HttpHelper.delete(url, token)
        val response = fromJson<Response>(json)
        return response
    }

    fun postFoto(file: File): String{
        // Converte para Base64
        val bytes = file.readBytes()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}