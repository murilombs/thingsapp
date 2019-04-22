package br.com.thingsproject.things.ui

import android.os.Bundle
import br.com.thingsproject.things.R
import br.com.thingsproject.things.base.BaseActivity
import br.com.thingsproject.things.dataClasses.Item
import br.com.thingsproject.things.domain.UserService
import br.com.thingsproject.things.extensions.loadUrl
import br.com.thingsproject.things.extensions.setupToolbar
import kotlinx.android.synthetic.main.activity_expends.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class MaisInformacoes : BaseActivity() {
    //var unidade: Item? = null
    private val unidade : Item by lazy { intent.getParcelableExtra<Item>("item") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mais_informacoes)
        setupToolbar(R.id.toolbarMI, unidade.name, false)
        initView()
    }

    private fun initView() {
        tNome.text = unidade.name
        showTC.text = unidade.timeCust
        //showDisponivel.text = unidade.delivery
        img_expands.loadUrl(unidade.itensImages)
        // o botão so pode aparecer se o user for o dono do item mostrado
        doAsync {
            val dados = UserService.getProfile(token)
            uiThread {
                if (unidade.owner == dados._id) {
                    editDelete.setOnClickListener {
                        startActivity<EditItem>("item" to unidade)
                    }
                } else {
                    editDelete.hide()
                }
            }
        }
    }
}
