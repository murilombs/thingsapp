package br.com.thingsproject.things.utils

import android.content.SharedPreferences
import br.com.thingsproject.things.Application

object Prefs {
val TOKEN = "acess"
    private fun prefs(): SharedPreferences {
        val context = Application.getInstance().applicationContext
        return context.getSharedPreferences(TOKEN, 0)
    }
    //armazena a chave-valor
    fun setInt(flag: String, valor: Int) = prefs().edit().putInt(flag, valor).apply()
    //Le a chave-valor
    fun getInt(flag: String) = prefs().getInt(flag, 0)

    fun setString(flag: String, valor: String) {
        //val pref = prefs()
        val editor = prefs().edit()
        editor.putString(flag, valor)
        editor.apply()
    }
    fun getString(flag: String): String {
        val pref = prefs()
        val s = pref.getString(flag, "")
        return s
    }
}