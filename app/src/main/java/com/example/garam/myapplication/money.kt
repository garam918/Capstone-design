package com.example.garam.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class money : AppCompatActivity() {

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

        }
        recycler.adapter = test
        test.notifyDataSetChanged()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
      //  val url = "https://map.kakao.com/link/to/${p1?.itemName},${p1?.mapPoint?.mapPointGeoCoord?.latitude}, ${p1?.mapPoint?.mapPointGeoCoord?.longitude}"
      //  val nextIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        val moneyText = findViewById<TextView>(R.id.moneyText)
        moneyText.text = getString(R.string.money)
    }
}
