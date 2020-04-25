package com.example.garam.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class money : AppCompatActivity(), View.OnClickListener {

    var lists = arrayListOf<moneyList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money)
        lists.add(moneyList("계양구청 : ","인천 계양구 계산새로 88"))
        lists.add(moneyList("남동구청 : ","인천 남동구 소래로 633"))
        lists.add(moneyList("미추홀구청 : ","인천 미추홀구 독정이로 95"))
        lists.add(moneyList("부평구청 : ","인천 부평구 부평대로 168"))
        lists.add(moneyList("서구청 : ","인천 서구 서곶로 299"))
        lists.add(moneyList("중구청 : ","인천 중구 신포로27번길 80"))

        val recycler = findViewById<RecyclerView>(R.id.moneyrecycler)

        val test = MoneyRecyclerAdapter(lists,this){
            moneyList ->
            val url = "https://map.kakao.com/link/to/${moneyList.name}"
            val nextIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(nextIntent)
        }
        recycler.adapter = test
        test.notifyDataSetChanged()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        val moneyText = findViewById<TextView>(R.id.moneyText)
        moneyText.text = getString(R.string.money)
        val text = findViewById<TextView>(R.id.gyeyang)
        val text2 = findViewById<TextView>(R.id.namdong)
        val text3 = findViewById<TextView>(R.id.michuhol)
        val text4 = findViewById<TextView>(R.id.bupyeong)
        val text5 = findViewById<TextView>(R.id.junggoo)
        val text6 = findViewById<TextView>(R.id.seogu)
        text.setOnClickListener(this)
        text2.setOnClickListener(this)
        text3.setOnClickListener(this)
        text4.setOnClickListener(this)
        text5.setOnClickListener(this)
        text6.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.gyeyang -> {
                Toast.makeText(this,"test",Toast.LENGTH_LONG).show()
            }
            R.id.namdong -> {
                Toast.makeText(this,"test2",Toast.LENGTH_LONG).show()
            }
            R.id.michuhol -> {

            }
            R.id.bupyeong -> {

            }
            R.id.junggoo -> {

            }
            R.id.seogu -> {

            }
        }
    }
}
