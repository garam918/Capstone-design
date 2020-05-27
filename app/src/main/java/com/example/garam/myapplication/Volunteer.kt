package com.example.garam.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.garam.myapplication.network.ApplicationController
import com.example.garam.myapplication.network.NetworkService
import com.google.gson.JsonObject
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_volunteer.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class Volunteer : AppCompatActivity() {
    private val networkService : NetworkService by lazy {
        ApplicationController.instance.networkService
    }
    private var backKeyPressedTime :Long= 0
    private var providerURI: Uri? = null
    private var mImage2 : MultipartBody.Part? = null
    override fun onBackPressed() {
        lateinit var toast: Toast
        if (System.currentTimeMillis() >= backKeyPressedTime + 1500) {
            backKeyPressedTime = System.currentTimeMillis()
            toast = Toast.makeText(this, "종료 하려면 한번 더 누르세요.", Toast.LENGTH_LONG)
            toast.show()
            return
        }
        if (System.currentTimeMillis() < backKeyPressedTime + 1500) {
            finish()
            moveTaskToBack(true)
            finishAndRemoveTask()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }
    lateinit var info: String
    private var mImage : MultipartBody.Part? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)
        val currentTime = Calendar.getInstance().time
        val date_text = SimpleDateFormat("yyyy년 MM월 dd일 EE요일", Locale.getDefault()).format(currentTime)
        val time = findViewById<TextView>(R.id.current)
        time.text = date_text
        val intent = intent
        info = intent.getStringExtra("info")
        //info = "만수종합사회복지관"
        val facility = findViewById<TextView>(R.id.facility)
        facility.text = "시설 정보 : $info"
        Log.e("시설","$info")
        val button = findViewById<Button>(R.id.button)
        val food = findViewById<Button>(R.id.food)
        val foodcam = findViewById<Button>(R.id.foodcamera)
        foodcam.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
            }
            val image = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val imageFile = File.createTempFile("IMG", ".jpg", cacheDir)
            providerURI = FileProvider.getUriForFile(
                this,
                "com.example.garam.myapplication.fileprovider",
                imageFile
            )
            image.putExtra(MediaStore.EXTRA_OUTPUT, providerURI)
            startActivityForResult(image, 40)
        }

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
            nextIntent.putExtra("Info",info)
            //startActivity(nextIntent)
            startActivityForResult(nextIntent,100)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        val foodimg = findViewById<ImageView>(R.id.foodimage)
        if(result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "등록된 정보가 없습니다.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Scanned:${result.contents}", Toast.LENGTH_LONG).show()
                try {
                    val foodamount = findViewById<TextView>(R.id.foodamount)
                    val img = File(result.barcodeImagePath)
                    val bitmap = BitmapFactory.decodeFile(img.absolutePath)
                    val byteArrayOutputSystem = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputSystem)
                    val photoBody = RequestBody.create("image/jpg".toMediaTypeOrNull(),byteArrayOutputSystem.toByteArray())
                    mImage = MultipartBody.Part.createFormData("qr","qr.jpg",photoBody)
                    val nameinfo = RequestBody.create("text/plain".toMediaTypeOrNull(),info)
                    Log.e("시설정보","$info")
                    val postimage: Call<String> = networkService.imageTest2(mImage,nameinfo)
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
                            foodamount.text = "현재 밥 양 : ${response.body()}인분 입니다."
                        }
                    })
                } catch (e: JSONException){
                      e.printStackTrace()
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
        if (resultCode == 100){
            val foodamount = findViewById<TextView>(R.id.foodamount)
            val mount = data?.getStringExtra("결과")
            val count : Int? = mount?.toInt()
            foodamount.text = "현재 밥 양 : ${count}인분 입니다."
        } else  if(requestCode == 40 && resultCode == RESULT_OK){
            performCrop()
        } else if (requestCode == 33){
            foodimg.setImageURI(providerURI)
            val options = BitmapFactory.Options()
            val inputStream: InputStream = contentResolver.openInputStream(providerURI)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val photoBody2 = RequestBody.create(
                "image/jpg".toMediaTypeOrNull(),
                byteArrayOutputStream.toByteArray()
            )
            mImage2 = MultipartBody.Part.createFormData(
                "menu",
                "menu.jpg",
                photoBody2
            )
            val nameinfo = RequestBody.create("text/plain".toMediaTypeOrNull(),info)

            val postImage : Call<String> = networkService.fda(nameinfo,mImage2)
            postImage.enqueue(object : Callback<String>{
                override fun onFailure(call: Call<String>, t: Throwable) {

                }

                override fun onResponse(call: Call<String>, response: Response<String>) {

                }
            })

        }
    }

    private fun performCrop(){
        val cropIntent = Intent("com.android.camera.action.CROP")

        cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        cropIntent.setDataAndType(providerURI,"image/*")
        cropIntent.putExtra("crop",true)
        cropIntent.putExtra("aspectX",1)
        cropIntent.putExtra("aspectY",1)
        cropIntent.putExtra("outputX",256)
        cropIntent.putExtra("outputY",256)
        cropIntent.putExtra("scale",true)
        cropIntent.putExtra("output",providerURI)
        cropIntent.putExtra("return-data",true)
        startActivityForResult(cropIntent,33)
    }
}