package com.example.garam.myapplication

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.garam.myapplication.network.ApplicationController
import com.example.garam.myapplication.network.NetworkService
import com.google.gson.JsonObject
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_volunteer.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class Volunteer : AppCompatActivity() {
    private val networkService : NetworkService by lazy {
        ApplicationController.instance.networkService
    }
    lateinit var info: String
    private var mImage : MultipartBody.Part? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)
        val intent = intent
        val face = intent.getStringExtra("face")
        val qrimg = intent.getStringExtra("QR")
     //   info = intent.getStringExtra("info")
        info = "만수종합사회복지관"
        Log.e("시설","$info")
        val button = findViewById<Button>(R.id.button)
        val food = findViewById<Button>(R.id.food)
        val iv = findViewById<ImageView>(R.id.imageView)
        Glide.with(this@Volunteer.applicationContext).load("http://f97ee76d.ngrok.io/$qrimg").error(R.drawable.ic_home_black_24dp).into(iv)
        button.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),100)
            val intentIntegrator = IntentIntegrator(this)
            intentIntegrator.setBeepEnabled(false)//바코드 인식시 소리
            intentIntegrator.setOrientationLocked(false)
            intentIntegrator.setBarcodeImageEnabled(true)
            intentIntegrator.captureActivity = AnyOrientationCaptureActivity::class.java
            intentIntegrator.setPrompt("사각형 안에 QR코드를 인식하여 주세요")
            intentIntegrator.initiateScan()
        }
        food.setOnClickListener {
            val nextIntent = Intent(this,Food::class.java)
        //    nextIntent.putExtra("Info",info)
            startActivity(nextIntent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "등록된 정보가 없습니다.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Scanned:${result.contents}", Toast.LENGTH_LONG).show()
                try {
                    val img = File(result.barcodeImagePath)
                    val bitmap = BitmapFactory.decodeFile(img.absolutePath)
                    val iv = findViewById<ImageView>(R.id.imageView)
               //     val obj = JSONObject()
                    //     obj.put("name",info)
                    iv.setImageBitmap(bitmap)
                    //     val jsonpost = obj.toString()
                    //  var obj = JSONObject(result.contents)
                    val qrpath = result.barcodeImagePath
                    val byteArrayOutputSystem = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputSystem)
                    val photoBody = RequestBody.create("image/jpg".toMediaTypeOrNull(),byteArrayOutputSystem.toByteArray())
                    mImage = MultipartBody.Part.createFormData("qr","qr.jpg",photoBody)
                    val imagefile = RequestBody.create("text/plain".toMediaTypeOrNull(),"qr")
                    val nameinfo = RequestBody.create("text/plain".toMediaTypeOrNull(),info)
                    Log.e("ㄹㅇㅋㄴㄹ",info)
                    Log.e("시설정보","$info")
                    val postimage: Call<String> = networkService.imageTest2(mImage,imagefile,nameinfo)
                    postimage.enqueue(object: Callback<String>
                    {
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.e("통신 실패","$t")
                        }

                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {
                           Log.e("통신 성공", response.body())
                        }
                    })
                    val nextIntent = Intent(this,popup::class.java)

                    nextIntent.putExtra("qrpath",qrpath)
                 //   startActivity(nextIntent)
                } catch (e: JSONException){
                      e.printStackTrace()
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
