package com.example.garam.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.myapplication.network.ApplicationController
import com.example.garam.myapplication.network.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuPic : AppCompatActivity() {
    private val networkService : NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_pic)
        var lists = arrayListOf<menuList>()
        var recycler = findViewById<RecyclerView>(R.id.recyclerView)
        val refresh2 = findViewById<Button>(R.id.refreshtwo)



        for ( i in 0 until 45){
            val count : Call<String> = networkService.food(resources.getStringArray(R.array.freefoodname)[i].toString())
            count.enqueue(object : Callback<String>{
                override fun onFailure(call: Call<String>, t: Throwable) {

                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    lists.add(menuList(resources.getStringArray(R.array.freefoodname)[i].toString(),response.body().toString()))
                }
            })
        }
        refresh2.setOnClickListener {

        }
        val test = MenuRecycler(lists, this) { menuList ->

        }
        recycler.adapter = test
        test.notifyDataSetChanged()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

    }
}
