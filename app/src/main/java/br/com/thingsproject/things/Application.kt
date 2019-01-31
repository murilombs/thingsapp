package br.com.thingsproject.things

import android.app.Application
import android.util.Log

class Application: Application() {
    private val TAG = "Aplication"
    //chamado quando o android criar o processo da aplicação
    override fun onCreate() {
        super.onCreate()
        //salva a instancia para termos acesso como singleton
        appInstance = this
    }

    companion object {
        //singleton da classe Application
        private var appInstance: Application? = null
        fun getInstance(): Application {
            if (appInstance == null) {
                throw IllegalAccessException("Configure a classe de acesso no AndroidManifest.xml")
            }
            return appInstance!!
        }
    }

    // Chamado quando o Android finalizar o processo da aplicação
    override fun onTerminate() {
        super.onTerminate()
        Log.d(TAG, "ThingsApplication.onTerminate()")
    }
}