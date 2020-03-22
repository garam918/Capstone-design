package com.example.garam.myapplication.ui.login

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.garam.myapplication.*
import com.example.garam.myapplication.network.ApplicationController
import com.example.garam.myapplication.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class LoginActivity : AppCompatActivity() {
    private var backKeyPressedTime: Long = 0
    private lateinit var loginViewModel: LoginViewModel
    private var providerURI: Uri? = null
    private var mImage : MultipartBody.Part? = null
    private var mAudio : MultipartBody.Part? = null
    private val networkService : NetworkService by lazy {
        ApplicationController.instance.networkService
    }
    override fun onBackPressed() {
        lateinit var toast: Toast
        if (System.currentTimeMillis() >= backKeyPressedTime + 1500) {
            backKeyPressedTime = System.currentTimeMillis()
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG)
            toast.show()
            return
        }
        if (System.currentTimeMillis() < backKeyPressedTime + 1500) {
            finish()
            moveTaskToBack(true)
            finishAndRemoveTask()
            android.os.Process.killProcess(android.os.Process.myPid())
            //           toast.cancel()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)
        val username = findViewById<EditText>(R.id.username)
        val userLogin = findViewById<Button>(R.id.userLogin)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val make = findViewById<Button>(R.id.makeRegi)
        val array = arrayOf("사용자","관리자")
        make.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setItems(array,DialogInterface.OnClickListener { dialog, which ->
                if (which == 0){
                    val nextIntent = Intent(this, QrcodeCreate::class.java)
                    startActivity(nextIntent)
                } else if (which ==1){
                    val nextIntent = Intent(this, ManagerCreate::class.java)
                    startActivity(nextIntent)
                }
            })
            builder.show()

            //val nextIntent = Intent(this, QrcodeCreate::class.java)
            //startActivity(nextIntent)
        }
        val loading = findViewById<ProgressBar>(R.id.loading)
        val obj = JSONObject()
        login.setOnClickListener {
        //    loading.visibility = View.VISIBLE
            val year = password.text.toString()
            obj.put("name","${username.text}")
            obj.put("year","$year")

            val json = obj.toString()
            val gsonObject = JsonParser().parse(json) as JsonObject
            val loginTry : Call<JsonObject> =  networkService.login(gsonObject)
            if (gsonObject != null) {
            loginTry.enqueue(object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("로그인", response.body()?.toString())
                      /*  val nname = response.body()!!.get("name").asString
                        val qr = response.body()!!.get("QR").asString
                        val date = response.body()!!.get("created_at").asString
                        val sex = response.body()!!.get("sex").asString
                        val gohome = response.body()!!.get("gohome").asString
                        val year = response.body()!!.get("year").asString
                        val face = response.body()!!.get("Faceimg").asString */
                        val place = response.body()!!.get("foodPlace").asString
                       // Log.e("이름",nname)
                        val nextIntent = Intent(this@LoginActivity, Volunteer::class.java)
                      //  val nextIntent = Intent(this@LoginActivity, fragmap::class.java)
                        nextIntent.putExtra("info","$place")
                        /*
                        nextIntent.putExtra("QR",qr)
                        nextIntent.putExtra("Face",face)
                        nextIntent.putExtra("name",nname)
                        nextIntent.putExtra("date",date)
                        nextIntent.putExtra("sex",sex)
                        nextIntent.putExtra("gohome",gohome)
                        nextIntent.putExtra("year",year)*/
                        Toast.makeText(this@LoginActivity,"로그인 성공!",Toast.LENGTH_LONG).show()
                        startActivity(nextIntent)

                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                }
            })
            } else if (gsonObject == null){

            }
        }
        val array2 = arrayOf("사진","음성")
        userLogin.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setItems(array2,DialogInterface.OnClickListener { dialog, which ->
                if (which == 0){
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
                } else if (which ==1){
                    val nextIntent = Intent(this,AudioRecord::class.java)
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 100)
                        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
                    }
                    startActivityForResult(nextIntent,99)

                }
            })
            builder.show()
        }
        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)
        loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer
            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })
        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 40){
            val options = BitmapFactory.Options()
            val inputStream: InputStream = contentResolver.openInputStream(providerURI)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)
            val photoBody2 = RequestBody.create(
                "image/*".toMediaTypeOrNull(),
                byteArrayOutputStream.toByteArray()
            )
          //  val imagefile = RequestBody.create("text/plain".toMediaTypeOrNull(), "face")
            mImage = MultipartBody.Part.createFormData(
                "face",
                "face.jpg",
                photoBody2
            )
            val postImage :Call<JsonObject> = networkService.imageLogin(mImage)
            postImage.enqueue(object : Callback<JsonObject>{
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("얼굴 실패","$t")
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    Log.e("얼굴 로그인","${response.body()}")

                    val nname = response.body()!!.get("name").asString
                    val qr = response.body()!!.get("QR").asString
                    val date = response.body()!!.get("created_at").asString
                    val sex = response.body()!!.get("sex").asString
                    val gohome = response.body()!!.get("gohome").asString
                    val year = response.body()!!.get("year").asString
                    val face = response.body()!!.get("Faceimg").asString
                    val nextIntent = Intent(this@LoginActivity,fragmap::class.java)
                    nextIntent.putExtra("name",nname)
                    nextIntent.putExtra("date",date)
                    nextIntent.putExtra("sex",sex)
                    nextIntent.putExtra("gohome",gohome)
                    nextIntent.putExtra("year",year)
                    nextIntent.putExtra("QR",qr)
                    nextIntent.putExtra("Face",face)
                    startActivity(nextIntent)
                }
            })

        }
        else if (requestCode == 99){

            val audioBody = RequestBody.create("audio/wav".toMediaTypeOrNull(),File(cacheDir.path + "/" + "recorder.wav"))
            Log.e("ㅇㄻ","$audioBody")
            mAudio = MultipartBody.Part.createFormData("voice", "voice.wav", audioBody)
            val audiofile = RequestBody.create("text/plain".toMediaTypeOrNull(),"voice")
            val postvoice : Call<ResponseBody> = networkService.audioLogin(mAudio)
            postvoice.enqueue(object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("에러","$t")
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.e("음성 로그인","${response.body()}")
                    val nextIntent = Intent (this@LoginActivity,fragmap::class.java)
                    startActivity(nextIntent)
                }
            })
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}