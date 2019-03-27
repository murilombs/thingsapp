package br.com.thingsproject.things.fragment

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.thingsproject.things.R
import br.com.thingsproject.things.adapter.TabsAdapter
import br.com.thingsproject.things.base.FragmentBasico
import br.com.thingsproject.things.domain.Abas
import br.com.thingsproject.things.extensions.toast
import br.com.thingsproject.things.ui.RegisterNewItem
import kotlinx.android.synthetic.main.fragment_itens.*
import kotlinx.android.synthetic.main.tabs_itens_list.*
import org.jetbrains.anko.startActivity

class ItensParticulares : FragmentBasico() {
    private var tipo = Abas.Meus //a aba que vai iniciar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null)
            tipo = arguments?.getSerializable("tipo") as Abas
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.tabs_itens_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.offscreenPageLimit = 2
        viewPager.adapter = TabsAdapter(context, childFragmentManager)
        viewPager.addOnPageChangeListener (object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    fab.show()
                } else {
                    fab.hide()
                }
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
        tabLayout.setupWithViewPager(viewPager)
    }
}
