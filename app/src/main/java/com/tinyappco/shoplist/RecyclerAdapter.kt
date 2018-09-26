package com.tinyappco.shoplist

import android.content.Intent
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.card_layout.view.*

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    var list = mutableListOf<ShoppingListItem>()

//    init{
//        list.add(ShoppingListItem("bread",2))
//        val cheese = ShoppingListItem("cheese",1)
//        cheese.purchased = true
//        list.add(cheese)
//
//    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup,false)
        return ViewHolder(view)}

    override fun getItemCount(): Int {
        return list.count()
    }

    fun removeFoundItems(){
        val iterator = list.iterator()
        while (iterator.hasNext()){
            val item = iterator.next()
            if (item.purchased){
                iterator.remove()
            }
        }
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.itemView.tvProduct.toggleStrikeThrough(false)
        holder.itemView.tvCount.toggleStrikeThrough(false)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val cardView = viewHolder.itemView
        val item = list[position]
        cardView.tvCount.text = item.count.toString()
        cardView.tvProduct.text = item.name

        if (item.purchased){
            cardView.tvProduct.toggleStrikeThrough(true)
            cardView.tvCount.toggleStrikeThrough(true)
        }
    }

    fun addItem(item: ShoppingListItem){
        list.add(item)
        notifyItemInserted(list.lastIndex)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init{
            itemView.setOnClickListener {
                list[adapterPosition].purchased = !list[adapterPosition].purchased
                toggleStrikeThrough(itemView.tvProduct, list[adapterPosition].purchased)
                toggleStrikeThrough(itemView.tvCount, list[adapterPosition].purchased)
            }
        }
    }


    private fun toggleStrikeThrough(textView: TextView, on: Boolean){
        if (on){
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }


}

