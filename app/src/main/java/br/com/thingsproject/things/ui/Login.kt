package br.com.thingsproject.things.ui

import android.os.Bundle
import android.widget.Toast
import br.com.thingsproject.things.R
import br.com.thingsproject.things.base.BaseActivity
import br.com.thingsproject.things.dataClasses.Autentifica
import br.com.thingsproject.things.dataClasses.Profiles
import br.com.thingsproject.things.domain.UserService.auth
import br.com.thingsproject.things.domain.dao.DaoService.salvarIntern
import br.com.thingsproject.things.extensions.fromJson
import kotlinx.android.synthetic.main.login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import br.com.thingsproject.things.utils.Prefs

class Login : BaseActivity() {
    val user : Autentifica? by lazy { intent.getParcelableExtra<Autentifica>("user") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val user_email = user_email.text
        val password = user_password.text

        btn_submit.setOnClickListener {
            val i = user?: Autentifica()
            i.email = user_email.toString()
            i.password = password.toString()

            doAsync {
                val response = auth(i)                    // obtem a resposta
                val dados = response.data.toString()         // obtem os dados e converte em string
                val userData = fromJson<Profiles>(dados)    // converte os dados em um obj Profile
                salvarIntern(userData)                              // salva internamente os dados
                Prefs.setString("token", response.token)       // salva o token
                uiThread {
                    Toast.makeText(this@Login,R.string.welcome, Toast.LENGTH_LONG).show()
                    startActivity<MainActivity>(dados to "user")    // envia os dados para a MainActivity
                }
            }
        }
        register_btn.setOnClickListener {
            startActivity<NewUser>()
        }
    }
}