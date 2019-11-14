package com.tinyappco.shoplist

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_layout.view.*

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    var list = mutableListOf<ShoppingListItem>()

    //must keep a refrence to this, otherwise lost
    //see: https://developer.android.com/reference/android/content/SharedPreferences#registerOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener)
    var prefsChangedListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->

        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        PreferenceManager.getDefaultSharedPreferences(recyclerView.context).registerOnSharedPreferenceChangeListener(prefsChangedListener)
    }

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

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val cardView = viewHolder.itemView
        val item = list[position]
        cardView.tvCount.text = item.count.toString()

        val prefs = PreferenceManager.getDefaultSharedPreferences(viewHolder.itemView.context)
        val hideSingleCount = prefs.getBoolean("hide_single_count",false)

        if (item.count == 1 && hideSingleCount){
            cardView.tvCount.visibility = View.GONE
        } else {
            cardView.tvCount.visibility = View.VISIBLE
        }

        cardView.tvProduct.text = item.name

        cardView.tvProduct.toggleStrikeThrough(item.purchased)
        cardView.tvCount.toggleStrikeThrough(item.purchased)
    }

    
    fun addItem(item: ShoppingListItem){
        list.add(item)
        notifyItemInserted(list.lastIndex)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init{
            itemView.setOnClickListener {
                list[adapterPosition].purchased = !list[adapterPosition].purchased
                itemView.tvProduct.toggleStrikeThrough(list[adapterPosition].purchased)
                itemView.tvCount.toggleStrikeThrough(list[adapterPosition].purchased)
            }
        }
    }
}

