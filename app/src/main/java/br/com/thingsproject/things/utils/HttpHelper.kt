package br.com.thingsproject.things.utils

import android.util.Log
import okhttp3.*
import java.io.IOException

object HttpHelper {
    private val TAG = "http"
    private val LOG_ON = true
    val JSON = MediaType.parse("application/json; charset=utf-8")
    var client = OkHttpClient()

    // ItensService
    // GET ItensService
    fun get(url: String): String {
        log("HttpHelper.get: $url")
        val request = Request.Builder().url(url).get().build()
        return getJson(request)
    }

    fun getItensUserID(url: String): String {
        log("HttpHelper.get: $url")
        val request = Request.Builder().url(url).get().build()
        return getJson(request)
    }

    // GET ItensService BUSCA POR ITENS COM ID DO PROPRIETARIO
    fun getItensForOwnerId(id: String, url: String): String {
        log("HttpHelper.getItensForOwnerId: $url")
        val request = Request.Builder().url(url).get()
                .header("header-view", id)
                .build()
        return getJson(request)
    }

    // POST ItensServices
    fun postU(url: String, token: String, json: String): String {
        log("HttpHelper.post: $url > $json")
        val body = RequestBody.create(JSON, json)
        val request = Request.Builder()
                .addHeader("access-token", token)
                .url(url)
                .post(body)
                .build()
        return getJson(request)
    }

    fun updateItens(url: String, token: String, json: String): String {
        log("HttpHelper.put: $url > $json")
        val body = RequestBody.create(JSON, json)
        val request = Request.Builder()
                .addHeader("access-token", token)
                .url(url)
                .put(body)
                .build()
        return getJson(request)
    }

    // DELETE ItensServices
    fun delete(url: String, token: String): String {
        log("HttpHelper.delete: $url")
        val request = Request.Builder().addHeader("access-token", token).url(url).delete().build()
        return getJson(request)
    }

    // UserServices
    // GET UserServices
    fun getU(url: String, token: String): String {
        log("HttpHelper.get: $url")
        val request = Request.Builder()
                .addHeader("access-token", token)
                .url(url)
                .get()
                .build()
        return getJson(request)
    }

    // POST UserServices
    fun post(url: String, json: String): String {
        log("HttpHelper.post: $url > $json")
        val body = RequestBody.create(JSON, json)
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        return getJson(request)
    }

    //Post UserServices
    fun postHeader(url: String, token: String): String {
        log("HttpHelper.post: $url > $token")
        val body = RequestBody.create(JSON, "")
        val request = Request.Builder()
                .addHeader("access-token", token)
                .url(url)
                .post(body)
                .build()
        return getJson(request)
    }

    // PUT UserServices
    fun put(url: String, token: String, dados: String): String {
        log("HttpHelper.put: $url > $dados")
        val body = RequestBody.create(JSON, dados)
        val request = Request.Builder().addHeader("access-token", token).url(url).put(body).build()
        return getJson(request)
    }

    // POST com parâmetros (form-urlencoded)
    fun postForm(url: String, params: Map<String, String>): String {
        log("HttpHelper.postForm: $url > $params")
        // Adiciona os parâmetros chave=valor na request POST
        val builder = FormBody.Builder()
        for ((key, value) in params) {
            builder.add(key, value)
        }
        val body = builder.build()
        // Faz a request
        val request = Request.Builder().url(url).post(body).build()
        return getJson(request)
    }

    // Lê a resposta do servidor no formato JSON
    private fun getJson(request: Request): String {
        val response = client.newCall(request).execute()
        val responseBody = response.body()
        if (responseBody != null) {
            val json = responseBody.string()
            log("	 << : $json")
            return json
        }
        throw IOException("Erro ao fazer a requisição")
    }

    private fun log(s: String) {
        if (LOG_ON) {
            Log.d(TAG, s)
        }
    }
}