package br.com.thingsproject.things.fragment

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.thingsproject.things.R
import br.com.thingsproject.things.adapter.CardAdapter
import br.com.thingsproject.things.base.FragmentBasico
import br.com.thingsproject.things.dataClasses.Item
import br.com.thingsproject.things.domain.ItensService
import org.jetbrains.anko.startActivity
import br.com.thingsproject.things.ui.MaisInformacoes
import br.com.thingsproject.things.ui.RegisterNewItem
import kotlinx.android.synthetic.main.fragment_itens.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

open class ItensFragement : FragmentBasico(){
    protected var itens = listOf<Item>()
    private var mLayoutManager : LinearLayoutManager? = null
    private val stateKey : String? = null

    //Infla a view do fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_itens, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLayoutManager = LinearLayoutManager(activity) //aq
        recyclerView?.layoutManager = mLayoutManager //aq
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.setHasFixedSize(true)
        tasks() //se não der certo transfira de volta ao onResume
    }

    override fun onResume() {
        super.onResume()
        fab.setOnClickListener {
            activity?.startActivity<RegisterNewItem>()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(stateKey, mLayoutManager?.onSaveInstanceState())
        //remover essa parte abaixo depois
        val saved = mLayoutManager?.onSaveInstanceState().toString()
        Log.d("SAVE_CHECK ", saved)
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val savedRecyclerViewState = savedInstanceState.getParcelable<Parcelable>(stateKey)
            if (savedRecyclerViewState != null) {
                mLayoutManager?.onRestoreInstanceState(savedRecyclerViewState)
            }
        }
        super.onViewStateRestored(savedInstanceState)
    }

    //busca os itens e posibilita os clicks nos cards
    open fun tasks() {
        doAsync {
            //busca os Itens
            itens = ItensService.getItens()
            uiThread {
                //Atualiza
                recyclerView.adapter = CardAdapter(itens) { onClickCard(it)}
            }
        }
    }

    open fun onClickCard(unidade: Item) {
        //click no card abre a Tela Mais Detalhes
        activity?.startActivity<MaisInformacoes>("item" to unidade)
    }
}