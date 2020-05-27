package com.example.garam.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Homeless : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homeless)

        val intent = intent
        val name = intent.getStringExtra("name")
        val date = intent.getStringExtra("date")
        val sex = intent.getStringExtra("sex")
        val gohome = intent.getStringExtra("gohome")
        val year = intent.getStringExtra("year")
        val faceimg = intent.getStringExtra("Face")
        val qrimg = intent.getStringExtra("QR")

        val userName = findViewById<TextView>(R.id.userName)
        val userInfo = findViewById<TextView>(R.id.userInfo)

        val map = findViewById<Button>(R.id.map)
        val freemoney = findViewById<Button>(R.id.money)
        val info = findViewById<Button>(R.id.info)
        val id = findViewById<Button>(R.id.id)
        val food = findViewById<Button>(R.id.food)
        val weather = findViewById<Button>(R.id.weather)

        map.setOnClickListener{
            val nextIntent = Intent(this, fragmap::class.java)
            nextIntent.putExtra("name",name)
            startActivity(nextIntent)
        }
        freemoney.setOnClickListener{
            val nextIntent = Intent(this, money::class.java)
            startActivity(nextIntent)
        }
        info.setOnClickListener{
            val nextIntent = Intent(this, policy::class.java)
            startActivity(nextIntent)
        }
        id.setOnClickListener{
            val nextIntent = Intent(this, popup::class.java)
            nextIntent.putExtra("name",name)
            nextIntent.putExtra("date",date)
            nextIntent.putExtra("sex",sex)
            nextIntent.putExtra("gohome",gohome)
            nextIntent.putExtra("year",year)
            nextIntent.putExtra("QR",qrimg)
            nextIntent.putExtra("Face",faceimg)
            startActivity(nextIntent)
        }
        food.setOnClickListener{
            val nextIntent = Intent(this, MenuPic::class.java)
            startActivity(nextIntent)
        }
        weather.setOnClickListener{
            val nextIntent = Intent(this, Weather::class.java)
            startActivity(nextIntent)
        }

    }

}
