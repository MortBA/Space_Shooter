package com.askan.spaceshooter.saved.scores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.askan.spaceshooter.R

// Define your item layout in item_layout.xml

// Create a ViewHolder
class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // Cache references to views within item layout
    val textView: TextView = itemView.findViewById(R.id.indivScore)
}

// Create an adapter
class RecyclerAdapter(private val itemList: List<String>) : RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_scores, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.textView.text = item
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}