package br.com.thingsproject.things.ui

import android.os.Bundle
import br.com.thingsproject.things.R
import br.com.thingsproject.things.base.BaseActivity
import br.com.thingsproject.things.dataClasses.Item
import br.com.thingsproject.things.extensions.loadUrl
import br.com.thingsproject.things.extensions.setupToolbar
import kotlinx.android.synthetic.main.activity_expends.*

class MaisInformacoes : BaseActivity() {
    //var unidade: Item? = null
    val unidade : Item by lazy { intent.getParcelableExtra<Item>("item") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mais_informacoes)
        setupToolbar(R.id.toolbarMI, unidade.name)
        //Le o item enviado como parametro
        //unidade = intent.getParcelableExtra("item") as Item
        //inicializa os dados na tela
        initView()
    }

    private fun initView() {
        tNome.text = unidade.name
        showTC.text = unidade.timeCust
        showDisponivel.text = unidade.delivery
        img_expands.loadUrl(unidade.itensImages)
    }
}
