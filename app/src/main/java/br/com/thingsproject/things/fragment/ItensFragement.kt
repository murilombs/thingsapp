package br.com.thingsproject.things.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.*
import br.com.thingsproject.things.R
import br.com.thingsproject.things.adapter.CardAdapter
import br.com.thingsproject.things.base.FragmentBasico
import br.com.thingsproject.things.dataClasses.Item
import br.com.thingsproject.things.domain.ItensService
import br.com.thingsproject.things.domain.Response
import org.jetbrains.anko.startActivity
import br.com.thingsproject.things.ui.MaisInformacoes
import br.com.thingsproject.things.ui.RegisterNewItem
import kotlinx.android.synthetic.main.fragment_itens.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

open class ItensFragement : FragmentBasico(), SearchView.OnQueryTextListener {
    protected var itens = listOf<Item>()
    private lateinit var mCallback : OnNewSearch

    //Infla a view do fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootview = inflater.inflate(R.layout.fragment_itens, container, false)
        val toolbar = rootview.findViewById<Toolbar>(R.id.toolbaR)
        if (toolbar != null) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }
        setHasOptionsMenu(true)
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.setHasFixedSize(true)
        // Tenho que resolver o problema da visualização do btn
        fab.setOnClickListener {
            activity?.startActivity<RegisterNewItem>()
        }
    }

    override fun onResume() {
        super.onResume()
        tasks()
    }

    // Set do botão de pesquisa
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_bar, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView : SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        /**
         * Talvez...
         * quando a pesquisa for feita talvez eu possa usar a interface
         * para comunicar e passar os dados para a MainActivity que por sua vez
         * vai abrir um novo fragment com a responsta procurada
         */
        doAsync {
            val itens = ItensService.getItensByName(query)
            uiThread {
                //Log.i(itens.status, itens.data.toString())
                mCallback.OnsearchDone(itens)
            }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    /**
     * Interface
     * Aqui eu implemento a interface que deve passar os dados List<Item>
     */
    interface OnNewSearch {
        fun OnsearchDone(item: List<Item>)
    }

    /**
     * onAttach...
     * assegura a implementação da Interface
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mCallback = context as OnNewSearch
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString()
                    + " must implement OnNewSearch")
        }
    }

    //busca os itens e posibilita os clicks nos cards
    open fun tasks() {
        object: Thread() {
            override fun run() {
                itens = ItensService.getItens()
                activity?.runOnUiThread(Runnable {
                    recyclerView.adapter = CardAdapter(itens) { onClickCard(it)}
                })
            }
        }.start()
    }

    open fun onClickCard(unidade: Item) {
        //click no card abre a Tela Mais Detalhes
        activity?.startActivity<MaisInformacoes>("item" to unidade)
    }
}