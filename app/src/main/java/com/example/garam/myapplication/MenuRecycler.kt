package com.example.garam.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.garam.myapplication.network.ApplicationController


class MenuRecycler (
    val items: ArrayList<menuList>,
    val context: Context,val itemClick: (menuList) -> Unit) : RecyclerView.Adapter<MenuRecycler.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.menulist, parent, false), itemClick
        )
        }
        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder?.bind(items[position])
        }

        inner class ViewHolder(itemView: View, itemClick: (menuList)-> Unit) : RecyclerView.ViewHolder(itemView){

            val textname = itemView?.findViewById<TextView>(R.id.foodwhich)
            val textyang = itemView?.findViewById<TextView>(R.id.foodyang)
            val imgUrl = ApplicationController.instance.baseURL
            val foodimg = itemView?.findViewById<ImageView>(R.id.foodimg)

            fun bind(list:menuList){
                textname.text = list.menuwhich
                textyang.text = "현재 밥 양은 ${list.menuyang}인분 입니다"
                Glide.with(context).load("$imgUrl/${list.menuwhich}.jpg").error(R.drawable.ic_home_black_24dp).skipMemoryCache(true).diskCacheStrategy(
                    DiskCacheStrategy.NONE).into(foodimg)

            }

        }
}
