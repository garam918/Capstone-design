package com.example.garam.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.garam.myapplication.network.ApplicationController

class popup : AppCompatActivity() {
    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_popup)

        val id = findViewById<TextView>(R.id.popup_id)
        val name = findViewById<TextView>(R.id.popup_name)
        val age = findViewById<TextView>(R.id.popup_age)
        val qr_img = findViewById<ImageView>(R.id.qr_img)
        val date = findViewById<TextView>(R.id.popup_date)
        val face = findViewById<ImageView>(R.id.faceimg)
        val home = findViewById<TextView>(R.id.goHome)
        val intent = intent
        val data_id = intent.getStringExtra("sex")
        val data_name = intent.getStringExtra("name")
        val data_age = intent.getStringExtra("year")
        val dateInfo = intent.getStringExtra("date")
        val faceimg = intent.getStringExtra("Face")
        val qrimg = intent.getStringExtra("QR")
        val gohome = intent.getStringExtra("gohome")
        val imgUrl = ApplicationController.instance.baseURL
        Glide.with(this).load("$imgUrl/$qrimg").error(R.drawable.ic_home_black_24dp).into(qr_img)
        Glide.with(this).load("$imgUrl/$faceimg").error(R.drawable.ic_home_black_24dp).into(face)
        id.text = "성별: $data_id"
        name.text = "이름: $data_name"
        age.text = "생년월일: $data_age"
        date.text = "회원가입 시간: $dateInfo"
        if (gohome == "false"){
            home.text = "귀향여비 사용 여부: X"
        } else if (gohome == "true"){
            home.text = "귀향여비 사용 여부: O"
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            if (event.action == MotionEvent.ACTION_OUTSIDE){
                return false
            }
        }
                return super.onTouchEvent(event)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        return
    }
}
