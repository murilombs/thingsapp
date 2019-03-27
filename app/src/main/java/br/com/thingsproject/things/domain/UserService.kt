package br.com.thingsproject.things.domain

import android.util.Base64
import br.com.thingsproject.things.dataClasses.Autentifica
import br.com.thingsproject.things.dataClasses.Profiles
import br.com.thingsproject.things.dataClasses.UpdateUser
import br.com.thingsproject.things.extensions.fromJson
import br.com.thingsproject.things.extensions.toJson
import br.com.thingsproject.things.utils.HttpHelper
import java.io.File

object UserService {
    private const val base_URL = "http://thingsproject.com.br/profiles"

    fun getProfile(token: String): Response {
        val json = HttpHelper.getU(base_URL, token)
        val profile = fromJson<Response>(json)
        return profile
    }

    fun auth(profile : Autentifica): Response {
        val url = "${base_URL}/authenticate/"
        val json = HttpHelper.post(url, profile.toJson())
        val response = fromJson<Response>(json)
        return response
    }

    fun refreshToken(token: String): Response {
        val url = "${base_URL}/refresh-token"
        val json = HttpHelper.postHeader(url, token)
        val response = fromJson<Response>(json)
        return response
    }

    fun postUser(profile: Profiles): Response {
        val json = HttpHelper.post(base_URL, profile.toJson())
        val response = fromJson<Response>(json)
        return response
    }

    fun updateUser(id : String, token: String, dados : UpdateUser): Response {
        val url = "${base_URL}/$id"
        val json = HttpHelper.put(url, token, dados.toJson())
        val response = fromJson<Response>(json)
        return response
    }
}