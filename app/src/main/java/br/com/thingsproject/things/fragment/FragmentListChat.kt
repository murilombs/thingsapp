package br.com.thingsproject.things.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.thingsproject.things.R
import br.com.thingsproject.things.base.FragmentBasico

class FragmentListChat : FragmentBasico() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat_list, container, false) //?
        return view
    }
}