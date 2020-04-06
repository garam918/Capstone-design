package com.example.garam.myapplication

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.garam.myapplication.network.ApplicationController
import com.example.garam.myapplication.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_qrcode_create.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*

class QrcodeCreate : AppCompatActivity() {
    private var providerURI: Uri? = null
    private var mImage : MultipartBody.Part? = null
    private var mImage2 : MultipartBody.Part? = null
    private var mAudio : MultipartBody.Part? = null
    private val networkService : NetworkService by lazy {
        ApplicationController.instance.networkService
    }
    private fun saveBitmapToJpeg(bitmap: Bitmap,name: String){
        val storage = cacheDir
        val fileName = "$name"
        val tempFile = File(storage,fileName)
        try{
            tempFile.createNewFile()
            val out = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
            out.close()
        }
        catch (e: FileNotFoundException){
            Log.e("FileNot", "FileNotFoundException$e")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode_create)
        val iv = findViewById<ImageView>(R.id.qrcode)
        val multiFormatWriter = MultiFormatWriter()
        val Create = findViewById<Button>(R.id.create)
        val camera = findViewById<Button>(R.id.camera)
        val yearSelect = findViewById<EditText>(R.id.year)
        val calSelect = findViewById<Button>(R.id.calsel)
        val audioRec = findViewById<Button>(R.id.audioRec)
        audioRec.setOnClickListener {
            val nextIntent = Intent(this,AudioRecord::class.java)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 100)
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
            }
            startActivity(nextIntent)
        }
        val cal = Calendar.getInstance()
        val mDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            var mm = "${month+1}"
            var doM = "$dayOfMonth"
            if (month < 10 ) {
                mm = "0${month+1}"
            }
            if ( dayOfMonth < 10){
                doM = "0$dayOfMonth"
            }
            var newYear = IntRange(2,3)
            yearSelect.setText("${year.toString().slice(newYear)}${mm}${doM}")
        }
        calSelect.setOnClickListener {
            DatePickerDialog(this,R.style.MySpinnerDatePickerStyle,mDateSetListener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE)).show()
        }
        camera.setOnClickListener {
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
            val obj = JSONObject()
            Create.setOnClickListener {
                try {
                    obj.put("name", name.text)
                    obj.put("year", year.text)
                    obj.put("sex", id.text)

                    if ( providerURI == null ){
                        Toast.makeText(this@QrcodeCreate,"사진을 촬영해주세요",Toast.LENGTH_LONG).show()
                    } else if ( "${name.text}" == "") {
                        Toast.makeText(this@QrcodeCreate,"이름을 입력하세요",Toast.LENGTH_LONG).show()
                    } else if ( "${id.text}" == "" ){
                        Toast.makeText(this@QrcodeCreate,"성별을 입력하세요",Toast.LENGTH_LONG).show()
                    } else if ( "${year.text}" == "" ){
                        Toast.makeText(this@QrcodeCreate,"생년월일을 입력하세요",Toast.LENGTH_LONG).show()
                    } else {
                        val json = obj.toString()
                        val gsonObject = JsonParser().parse(json) as JsonObject
                        Toast.makeText(this, json, Toast.LENGTH_LONG).show()
                        val hints = Hashtable<EncodeHintType, String>()
                        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
                        val bitMatrix =
                            multiFormatWriter.encode(json, BarcodeFormat.QR_CODE, 200, 200, hints)
                        val barcodeEncoder = BarcodeEncoder()
                        val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                        iv.setImageBitmap(bitmap)
                        saveBitmapToJpeg(bitmap, name.text.toString())
                        val path = cacheDir.path + "/" + "${name.text}"
                        val testBit = BitmapFactory.decodeFile(path)
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        testBit.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                        val photoBody = RequestBody.create(
                            "image/*".toMediaTypeOrNull(),
                            byteArrayOutputStream.toByteArray()
                        )
                        mImage = MultipartBody.Part.createFormData(
                            "qr",
                            "qr.jpg",
                            photoBody
                        )
                        val options = BitmapFactory.Options()
                        val inputStream: InputStream =
                            contentResolver.openInputStream(providerURI)
                        val bitmap2 = BitmapFactory.decodeStream(inputStream, null, options)
                        val byteArrayOutputStream2 = ByteArrayOutputStream()
                        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream2)
                        val photoBody2 = RequestBody.create(
                            "image/*".toMediaTypeOrNull(),
                            byteArrayOutputStream2.toByteArray()
                        )
                        val audioBody = RequestBody.create("audio/mp3".toMediaTypeOrNull(),File(cacheDir.path + "/" + "recorder.mp3"))
                        mImage2 = MultipartBody.Part.createFormData(
                            "face",
                            "${year.text}" + ".jpg",
                            photoBody2
                        )
                        mAudio = MultipartBody.Part.createFormData(
                            "audio", "${name.text}.mp3", audioBody
                        )
                        val imagefile2 =
                            RequestBody.create("text/plain".toMediaTypeOrNull(), "qr")
                        val testJson = gsonObject.toString()
                        val jsonpost =
                            RequestBody.create("text/plain".toMediaTypeOrNull(), testJson)
                        val imagefile =
                            RequestBody.create("text/plain".toMediaTypeOrNull(), "face")
                        val audiofile = RequestBody.create("text/plain".toMediaTypeOrNull(),"audio")
                        Log.e("json",testJson)
                        val postimage: Call<ResponseBody> = networkService.imageTest4(
                            jsonpost,
                            mImage,
                            mImage2,
                            mAudio,
                            imagefile,
                            imagefile2,
                            audiofile
                        )
                        postimage.enqueue(object : Callback<ResponseBody> {
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Log.e("write fail", t.toString())
                            }

                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        this@QrcodeCreate,
                                        "회원가입에 성공했습니다.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    finish()
                                }
                            }
                        })
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val iv = findViewById<ImageView>(R.id.qrcode)
        if(requestCode == 40 && resultCode == RESULT_OK){
            performCrop()
            iv.setImageURI(providerURI)
        } else if (requestCode == 33){

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