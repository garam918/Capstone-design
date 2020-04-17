package com.example.garam.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.garam.myapplication.ui.login.LoginActivity
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseMessaging.getInstance().subscribeToTopic("test").addOnCompleteListener {task->
            if (task.isSuccessful) {
                Log.e("Test","구독 요청 성공")
            } else {
                Log.e("Test", "구독 요청 실패")
            }

        }
        val login = Intent(this,LoginActivity::class.java)
        startActivity(login)
        val volunteer = findViewById<Button>(R.id.volunteer)
        volunteer.setOnClickListener {
            val nextIntent = Intent(this,Volunteer::class.java)
            startActivity(nextIntent)
        }
        val frag = findViewById<Button>(R.id.frag)
        frag.setOnClickListener {
            val nextIntent = Intent(this, fragmap::class.java)
            startActivity(nextIntent)
        }

    }

}
