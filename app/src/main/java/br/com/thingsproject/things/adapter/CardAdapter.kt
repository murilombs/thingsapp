package br.com.thingsproject.things.adapter

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.thingsproject.things.R
import br.com.thingsproject.things.dataClasses.Item
import br.com.thingsproject.things.extensions.loadUrl
import kotlinx.android.synthetic.main.adapter_card.view.*

//construtor
class CardAdapter (val itens: List<Item>, val onClick: (Item) -> Unit): RecyclerView.Adapter<CardAdapter.CardsViewHolder>() {
    //retorna a quantia de itens na lista
    override fun getItemCount() = this.itens.size

    //infla o adapter e retorna o viewholder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        //infla a view do adapter
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_card, parent, false)
        //retorna a viewholder com todas a views
        val holder = CardsViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        val item = itens[position]
        val view = holder.itemView
        with(view) {
            //atualiza os dados
            view.tName.text = item.name
            //download da foto e progressbar
            view.img.loadUrl(item.itensImages, progress)
            //botão
            view.BtnCusto.text = item.timeCust
            //evento clique
            setOnClickListener { onClick(item) }
        }
    }

    class CardsViewHolder(view: View): RecyclerView.ViewHolder(view)
}