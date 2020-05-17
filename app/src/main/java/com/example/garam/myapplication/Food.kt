package com.example.garam.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doBeforeTextChanged
import com.example.garam.myapplication.network.ApplicationController
import com.example.garam.myapplication.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Food : AppCompatActivity() {

    private val networkService : NetworkService by lazy {
        ApplicationController.instance.networkService
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)
        val intent = intent
        val info = intent.getStringExtra("Info")
        Log.e("시설","$info")
        val obj = JSONObject()
        val firstfood = findViewById<EditText>(R.id.foodedit)
        val foodButton = findViewById<Button>(R.id.foodButton)
        foodButton.isEnabled = false
        firstfood.doBeforeTextChanged { text, start, count, after ->
            foodButton.isEnabled = true
        }
        foodButton.setOnClickListener {
            obj.put("name",info)
            val foodcount = Integer.parseInt(firstfood.text.toString())
            obj.put("food",foodcount)
            val json = obj.toString()
            val gsonObject = JsonParser().parse(json) as JsonObject
            val foodSetting: Call<JsonObject> = networkService.foodTest(gsonObject)
            foodSetting.enqueue(object : Callback<JsonObject>{
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("테스트","${response.body()}")
                    finish()
                }
            })
            val data = intent
            data.putExtra("결과","${firstfood.text}")

            setResult(100,data)
        }
    }
}
