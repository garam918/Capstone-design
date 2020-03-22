package com.example.garam.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.garam.myapplication.network.ApplicationController
import com.example.garam.myapplication.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_manager_create.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManagerCreate : AppCompatActivity() {

    private val networkService : NetworkService by lazy {
        ApplicationController.instance.networkService
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_create)
        val singup = findViewById<Button>(R.id.manage)
        val obj = JSONObject()
        singup.setOnClickListener {
            obj.put("managername", manageName.text)
            obj.put("pw", managePwd.text)
            obj.put("foodPlace", manageInfo.text)
            if ("${manageName.text}" == "") {
                Toast.makeText(this, "이름을 입력하세요", Toast.LENGTH_LONG).show()
            } else if ("${managePwd.text}" == "") {
                Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_LONG).show()
            } else if ("${manageInfo.text}" == "") {
                Toast.makeText(this, "시설정보를 입력하세요", Toast.LENGTH_LONG).show()
            } else if (managePwd.length() < 6) {
                Toast.makeText(this,"비밀번호를 6자리 이상 입력하세요",Toast.LENGTH_LONG).show()
            }
            else {
                val json = obj.toString()
                val gsonObject = JsonParser().parse(json) as JsonObject
                val managerSign: Call<JsonObject> = networkService.manager(gsonObject)
                managerSign.enqueue(object : Callback<JsonObject>{
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        Log.e("로그","${response.body()}")
                        Toast.makeText(this@ManagerCreate,"회원가입에 성공했습니다.",Toast.LENGTH_LONG).show()
                        finish()
                    }
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    }
                })
            }
        }
    }
}
