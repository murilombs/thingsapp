package br.com.thingsproject.things.ui

import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import br.com.thingsproject.things.R
import br.com.thingsproject.things.base.BaseActivity
import br.com.thingsproject.things.dataClasses.Profiles
import br.com.thingsproject.things.domain.UserService.postUser
import br.com.thingsproject.things.domain.dao.DaoService.salvarIntern
import br.com.thingsproject.things.utils.AccesValidator
import br.com.thingsproject.things.utils.Prefs
import kotlinx.android.synthetic.main.new_user.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class NewUser : BaseActivity() {
    private val user: Profiles? by lazy { intent.getParcelableExtra<Profiles>("user") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_user)
        initViews()
    }

    private fun initViews() {
        user.apply {
            val name = user_name.text
            val lastname = user_second_name.text
            val email = user_email.text
            val password1 = user_password_1.text
            val password2 = user_password_2.text

            btn_submit.setOnClickListener {
                task(name, lastname, email, password1, password2)
            }
        }
    }

    private fun task(name : Editable ,lastname : Editable, email : Editable, password1 : Editable, password2 : Editable) {
        val i = user ?: Profiles()
        val contract = AccesValidator()
        if (password1.toString() != password2.toString()) {
            Toast.makeText(this@NewUser, R.string.pass_res_err, Toast.LENGTH_LONG).show()
        } else {
            i.first_name = name.toString()
            i.second_name = lastname.toString()
            i.email = email.toString()
            i.password = password1.toString()

            contract.isRequire(i.first_name, context)
            contract.isRequire(i.second_name, context)
            contract.isEmail(i.email, context)
            contract.hasMinlen(i.password, 8, context)

            if (!contract.isValid()) {
                //Aqui ficara a tratativa do response
                contract.ValidationContract()
                //Toast.makeText(this@NewUser,R.string.form_res_err, Toast.LENGTH_LONG).show()
            } else {
                doAsync {
                    val response = postUser(i) // posta o user e obtem a res da API
                    Prefs.setString("token", response.token) // armazena o token
                    salvarIntern(i) // salva internamente os dados do user
                    uiThread {
                        toast(response.message)
                        startActivity<MainActivity>()
                    }
                }
            }
        }
    }
}