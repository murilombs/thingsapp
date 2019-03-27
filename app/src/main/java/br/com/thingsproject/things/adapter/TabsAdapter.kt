package br.com.thingsproject.things.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import br.com.thingsproject.things.domain.Abas
import br.com.thingsproject.things.fragment.ItensList

class TabsAdapter(private val context: Context?, fm : FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = 2

    //Retornal qual pela posição
    private fun getAbaItens(position: Int) = when(position) {
        0 -> Abas.Meus
        else -> Abas.Alugados
    }

    //titulo
    override fun getPageTitle(position: Int): CharSequence? {
        val tipo = getAbaItens(position)
        return context?.getString(tipo.string)
    }

    override fun getItem(position: Int): Fragment {
        val tipo = getAbaItens(position)
        val f: Fragment = ItensList()
        val arguments = Bundle()
        arguments.putSerializable("tipo", tipo)
        f.arguments = arguments
        return f
    }
}