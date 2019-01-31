package br.com.thingsproject.things.domain

import com.google.gson.JsonObject

data class Response(val id: Long, val status: String, val message: String, val token: String, val data: JsonObject) {
    fun isOK() = "OK".equals(status, ignoreCase = true)
}

data class ResponseProfiles(
        val _id: String,
        val status: String,
        val first_name: String,
        val second_name: String,
        val profilePicture: String,
        val token : String,
        val data : JsonObject) {
    fun isOk() = "OK".equals(status, ignoreCase = true)
}