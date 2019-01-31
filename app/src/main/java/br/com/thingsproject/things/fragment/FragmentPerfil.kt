package br.com.thingsproject.things.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.thingsproject.things.R
import br.com.thingsproject.things.base.FragmentBasico
import br.com.thingsproject.things.domain.UserService.getProfile
import br.com.thingsproject.things.extensions.loadUrl
import br.com.thingsproject.things.ui.UpdateInfoUser
import br.com.thingsproject.things.utils.Prefs
import kotlinx.android.synthetic.main.fragment_perfil.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.uiThread

class FragmentPerfil : FragmentBasico() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        task()
    }

    private fun task() {
        doAsync {
            val t = Prefs.getString("token") // recupera o token do cache
            val dados = getProfile(t) // usa o token para o link da img
            //val profile = getIntern() // pega os dados internos da Room
            uiThread {
                f_nome.text = dados.first_name
                s_nome.text = dados.second_name
                image_profile.loadUrl(dados.profilePicture)
            }
        }
        fab.setOnClickListener {
            startActivity<UpdateInfoUser>()
        }
    }
}