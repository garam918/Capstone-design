package com.example.garam.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MoneyRecyclerAdapter (val item : ArrayList<moneyList>,val context:Context,
val itemClick : (moneyList)-> Unit): RecyclerView.Adapter<MoneyRecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoneyRecyclerAdapter.ViewHolder {
       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.freemoney,parent,false),itemClick)
    }

    override fun onBindViewHolder(holder: MoneyRecyclerAdapter.ViewHolder, position: Int) {
        holder?.bind(item[position])

    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class ViewHolder(itemView: View,itemClick: (moneyList) -> Unit): RecyclerView.ViewHolder(itemView){
        val moneyName = itemView?.findViewById<TextView>(R.id.moneyName)
        val moneyAddress = itemView?.findViewById<TextView>(R.id.moneyAddress)
        fun bind(list: moneyList){
            moneyName.text = list.name
            moneyAddress.text = list.address
            itemView.setOnClickListener { itemClick(list) }
        }

    }
}