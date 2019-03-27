package br.com.thingsproject.things.domain

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

data class Response(val id: Long,
                    val _id: String,
                    val status: String,
                    val message: String,
                    val token: String,
                    val first_name: String,
                    val second_name: String,
                    val profilePicture: String,
                    val data : JsonElement) {
    fun isOK() = "OK".equals(status, ignoreCase = true)
}