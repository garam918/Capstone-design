package com.example.garam.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StdRecyclerAdapter(
    val items: ArrayList<polList>,
    val context: Context,val itemClick: (polList) -> Unit) : RecyclerView.Adapter<StdRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item,parent,false),itemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bind(items[position])
    }

    inner class ViewHolder (itemView: View, itemClick: (polList)-> Unit) : RecyclerView.ViewHolder(itemView) {
        // Holds the TextView that will add each animal to
        val textlist = itemView?.findViewById<TextView>(R.id.textlist)
        val textName = itemView?.findViewById<TextView>(R.id.textName)

        fun bind (list: polList){
            textlist.text = list.list
            textName.text = list.name
            itemView.setOnClickListener { itemClick(list) }
        }
    }


}
