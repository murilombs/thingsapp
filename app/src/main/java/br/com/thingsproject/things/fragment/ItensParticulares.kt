package br.com.thingsproject.things.fragment

import android.os.Bundle
import android.util.Log
import br.com.thingsproject.things.adapter.CardAdapter
import br.com.thingsproject.things.dataClasses.Item
import br.com.thingsproject.things.domain.Abas
import br.com.thingsproject.things.ui.MaisInformacoes
import kotlinx.android.synthetic.main.fragment_itens.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jetbrains.anko.startActivity

class ItensParticulares : ItensFragement() {
    private var tipo = Abas.Meus //a aba que vai iniciar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null)
            tipo = arguments?.getSerializable("tipo") as Abas
    }

    /*
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tabs_itens_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewTabs()
    }

    //inicia o viewPager e os tabs
    private fun setupViewTabs() {
        viewPager.offscreenPageLimit = 2
        viewPager.adapter = TabsAdapter(context, fragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        //val cor = ContextCompat.getColor(context, R.color.branco)
        //tabLayout.setTabTextColors(cor, cor)
    }
    */

    override fun tasks() {
        doAsync {

        }
    }

    override fun onClickCard(unidade: Item) {
        //Aqui entra a tela de mais informações
        activity?.startActivity<MaisInformacoes>("item" to unidade)
    }
}
