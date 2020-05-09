package com.example.garam.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class money : AppCompatActivity(), View.OnClickListener {

    lateinit var geyphone: String
    lateinit var namphone: String
    lateinit var miphone: String
    lateinit var boophone: String
    lateinit var seophone: String
    lateinit var jungphone: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money)
        val moneyText = findViewById<TextView>(R.id.moneyText)
        moneyText.text = getString(R.string.money)

        val text10 = findViewById<TextView>(R.id.geyAdd)
        val text11 = findViewById<TextView>(R.id.namAdd)
        val text12 = findViewById<TextView>(R.id.miAdd)
        val text13 = findViewById<TextView>(R.id.booAdd)
        val text14 = findViewById<TextView>(R.id.seoAdd)
        val text15 = findViewById<TextView>(R.id.jungAdd)

        text10.setOnClickListener(this)
        text11.setOnClickListener(this)
        text12.setOnClickListener(this)
        text13.setOnClickListener(this)
        text14.setOnClickListener(this)
        text15.setOnClickListener(this)

        val text20 = findViewById<TextView>(R.id.phoneGey)
        val text21 = findViewById<TextView>(R.id.phoneNam)
        val text22 = findViewById<TextView>(R.id.phoneBoo)
        val text23 = findViewById<TextView>(R.id.phoneSeo)
        val text24 = findViewById<TextView>(R.id.phoneMi)
        val text25 =findViewById<TextView>(R.id.phoneJung)


        text20.setOnClickListener(this)
        text21.setOnClickListener(this)
        text22.setOnClickListener(this)
        text23.setOnClickListener(this)
        text24.setOnClickListener(this)
        text25.setOnClickListener(this)


        geyphone = "tel:" + text20.text.toString()
        namphone = "tel:" + text21.text.toString()
        boophone = "tel:" + text22.text.toString()
        seophone = "tel:" + text23.text.toString()
        miphone = "tel:" + text24.text.toString()
        jungphone = "tel:" + text25.text.toString()
    }

    @SuppressLint("ResourceType")
    override fun onClick(v: View?) {
        when (v?.id){

            R.id.geyAdd -> {
                val url = "https://map.kakao.com/link/to/계양구청,37.5375028862693,126.737709165273"
                val nextIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(nextIntent)
            }
            R.id.namAdd -> {
                val url = "https://map.kakao.com/link/to/남동구청,37.447348790365,126.731487739738"
                val nextIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(nextIntent)
            }
            R.id.miAdd -> {
                val url = "https://map.kakao.com/link/to/미추홀구청,37.4635932866927,126.650573941082"
                val nextIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(nextIntent)
            }
            R.id.booAdd -> {
                val url = "https://map.kakao.com/link/to/부평구청,37.5070416849512,126.721873215514"
                val nextIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(nextIntent)
            }
            R.id.seoAdd -> {
                val url = "https://map.kakao.com/link/to/서구청,37.5443029960023,126.676440722255"
                val nextIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(nextIntent)
            }
            R.id.jungAdd -> {
                val url = "https://map.kakao.com/link/to/중구청,37.4736641342665,126.621704004425"
                val nextIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(nextIntent)
            }

            R.id.phoneGey -> {
                val uri = Uri.parse(geyphone)
                val intent = Intent(Intent.ACTION_DIAL,uri)
                startActivity(intent)
            }
            R.id.phoneBoo -> {
                val uri = Uri.parse(boophone)
                val intent = Intent(Intent.ACTION_DIAL,uri)
                startActivity(intent)
            }
            R.id.phoneNam -> {
                val uri = Uri.parse(namphone)
                val intent = Intent(Intent.ACTION_DIAL,uri)
                startActivity(intent)
            }
            R.id.phoneMi -> {
                val uri = Uri.parse(miphone)
                val intent = Intent(Intent.ACTION_DIAL,uri)
                startActivity(intent)
            }
            R.id.phoneSeo -> {
                val uri = Uri.parse(seophone)
                val intent = Intent(Intent.ACTION_DIAL,uri)
                startActivity(intent)
            }
            R.id.phoneJung -> {
                val uri = Uri.parse(jungphone)
                val intent = Intent(Intent.ACTION_DIAL,uri)
                startActivity(intent)
            }

        }
    }
}
