package br.com.thingsproject.things.ui

import android.os.Bundle
import android.view.View
import br.com.thingsproject.things.R
import br.com.thingsproject.things.base.BaseActivity
import br.com.thingsproject.things.dataClasses.Item
import br.com.thingsproject.things.extensions.loadUrl
import br.com.thingsproject.things.extensions.onClick
import br.com.thingsproject.things.extensions.setupToolbar
import com.getbase.floatingactionbutton.*
import kotlinx.android.synthetic.main.activity_expends.*

class MaisInformacoes : BaseActivity() {
    //var unidade: Item? = null
    val unidade : Item by lazy { intent.getParcelableExtra<Item>("item") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mais_informacoes)
        setupToolbar(R.id.toolbarMI, unidade.name)
        initView()
    }

    private fun initView() {
        tNome.text = unidade.name
        showTC.text = unidade.timeCust
        //showDisponivel.text = unidade.delivery
        img_expands.loadUrl(unidade.itensImages)
        editDelete.setOnClickListener {
            if(!editDelete.isExpanded){
                editDelete.expand()
            } else {
                editDelete.collapse()
            }
        }
    }
}
