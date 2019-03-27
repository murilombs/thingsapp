package br.com.thingsproject.things.fragment

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.thingsproject.things.R
import br.com.thingsproject.things.adapter.CardAdapter
import br.com.thingsproject.things.dataClasses.Item
import br.com.thingsproject.things.extensions.fromJson
import kotlinx.android.synthetic.main.fragment_itens.*

/** Mostra o retorno da pesquisa */

class ItensListConsult : ItensList() {
    lateinit var item : String // vai receber o parametro
    lateinit var list : List<Item> // lista com os itens

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            item = bundle.getString("argument") // e inicializada aqui
        }
    }

    override fun tasks() {
        list = fromJson<List<Item>>(item) // a lista e carregada aqui
        object : Thread() {
            override fun run() {
                activity?.runOnUiThread(Runnable {
                    recyclerView.adapter = CardAdapter(list) { onClickCard(it) }
                })
            }
        }.start()
    }
}