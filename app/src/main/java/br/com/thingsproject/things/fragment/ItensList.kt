package br.com.thingsproject.things.fragment

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.thingsproject.things.R
import br.com.thingsproject.things.adapter.CardAdapter
import br.com.thingsproject.things.domain.ItensService
import br.com.thingsproject.things.domain.UserService
import kotlinx.android.synthetic.main.fragment_itens.*

open class ItensList: ItensFragement() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootview = inflater.inflate(R.layout.fragment_itens, container, false)
        val toolbar = rootview.findViewById<Toolbar>(R.id.toolbaR)
        toolbar.visibility = View.GONE // esconde a toolbar nas abas
        return rootview
    }

    //busca os itens e posibilita os clicks nos cards
    override fun tasks() {
        object: Thread() {
            override fun run() {
                val dados = UserService.getProfile(token)
                itens = ItensService.getItensIDUser(dados._id)
                activity?.runOnUiThread(Runnable {
                    recyclerView.adapter = CardAdapter(itens) { onClickCard(it)}
                })
            }
        }.start()
    }
}