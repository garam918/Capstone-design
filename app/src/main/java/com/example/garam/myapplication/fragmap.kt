package com.example.garam.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.garam.myapplication.R.layout.activity_fragmap
import com.example.garam.myapplication.network.ApplicationController
import com.example.garam.myapplication.network.KakaoApi
import com.example.garam.myapplication.network.NetworkService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class fragmap : AppCompatActivity(), MapView.POIItemEventListener, MapView.MapViewEventListener {
    private var backKeyPressedTime :Long= 0

    val array2 = arrayOf(MapPoint.mapPointWithGeoCoord(37.4498013	,126.739349),
        MapPoint.mapPointWithGeoCoord(37.4635492	,126.725459),
        MapPoint.mapPointWithGeoCoord(37.4478231	,126.737056),
        MapPoint.mapPointWithGeoCoord(37.465821	,126.697073),
        MapPoint.mapPointWithGeoCoord(37.450981	,126.697239),
        MapPoint.mapPointWithGeoCoord(37.4630848	,126.736847),
        MapPoint.mapPointWithGeoCoord(37.4593334	,126.726309),
        MapPoint.mapPointWithGeoCoord(37.390999	,126.714991),
        MapPoint.mapPointWithGeoCoord(37.405135	,126.7366397),
        MapPoint.mapPointWithGeoCoord(37.43042634	,126.7438364),
        MapPoint.mapPointWithGeoCoord(37.4543626	,126.7247717),
        MapPoint.mapPointWithGeoCoord(37.418596	,126.6888374),
        MapPoint.mapPointWithGeoCoord(37.4228258	,126.6649553),
        MapPoint.mapPointWithGeoCoord(37.42110118	,126.6877625),
        MapPoint.mapPointWithGeoCoord(37.4146304	,126.6873176),
        MapPoint.mapPointWithGeoCoord(37.4276233	,126.6665919),
        MapPoint.mapPointWithGeoCoord(37.4188256	,126.6963623),
        MapPoint.mapPointWithGeoCoord(37.4211514	,126.6919335),
        MapPoint.mapPointWithGeoCoord(37.37161237	,126.6598566),
        MapPoint.mapPointWithGeoCoord(37.46781706	,126.633861),
        MapPoint.mapPointWithGeoCoord(37.47444945	,126.6272621),
        MapPoint.mapPointWithGeoCoord(37.74032225	,126.4912094),
        MapPoint.mapPointWithGeoCoord(37.47837347	,126.6445584),
        MapPoint.mapPointWithGeoCoord(37.47892775	,126.6302205),
        MapPoint.mapPointWithGeoCoord(37.47570837	,126.6357077),
        MapPoint.mapPointWithGeoCoord(37.4710471	,126.6411836),
        MapPoint.mapPointWithGeoCoord(37.4802538	,126.6283243),
        MapPoint.mapPointWithGeoCoord(37.4456411	,126.6703156),
        MapPoint.mapPointWithGeoCoord(37.4574457	,126.6895176),
        MapPoint.mapPointWithGeoCoord(37.4655182	,126.6827563),
        MapPoint.mapPointWithGeoCoord(37.4588806	,126.6924042),
        MapPoint.mapPointWithGeoCoord(37.53267769	,126.4270352),
        MapPoint.mapPointWithGeoCoord(37.6616438	,125.7032019),
        MapPoint.mapPointWithGeoCoord(37.93442358	,124.6837937),
        MapPoint.mapPointWithGeoCoord(37.82536146	,124.7141543),
        MapPoint.mapPointWithGeoCoord(37.22648044	,126.1467783),
        MapPoint.mapPointWithGeoCoord(37.25406432	,126.3071493),
        MapPoint.mapPointWithGeoCoord(37.25609956	,126.4833427),
        MapPoint.mapPointWithGeoCoord(37.5478142	,126.7258585),
        MapPoint.mapPointWithGeoCoord(37.5309531	,126.709125),
        MapPoint.mapPointWithGeoCoord(37.5570114	,126.7533119),
        MapPoint.mapPointWithGeoCoord(37.5435316	,126.7155969),
        MapPoint.mapPointWithGeoCoord(37.5286514	,126.734723),
        MapPoint.mapPointWithGeoCoord(37.5774578	,126.736695),
        MapPoint.mapPointWithGeoCoord(37.5261558	,126.75011),
        MapPoint.mapPointWithGeoCoord(37.5107467	,126.6758292),
        MapPoint.mapPointWithGeoCoord(37.4955378	,126.6912229),
        MapPoint.mapPointWithGeoCoord(37.5462409	,126.6721504),
        MapPoint.mapPointWithGeoCoord(37.6020407	,126.6614441))
    val sangdam: Map<Int,String> = mapOf(0  to "인천내일을여는집 쪽방상담소",
        1 to "연수구중독관리통합지원센터")

    val sangdam2 = arrayOf(MapPoint.mapPointWithGeoCoord(37.5478266028925,126.725849880728),
        MapPoint.mapPointWithGeoCoord(37.4121365721784,126.665985810103))

    val jobpoint = arrayOf(MapPoint.mapPointWithGeoCoord(37.4687584336674,126.660821349251),
    MapPoint.mapPointWithGeoCoord(37.4051962463669,126.695946205678),
    MapPoint.mapPointWithGeoCoord(37.4740806732311,126.621339693079),
    MapPoint.mapPointWithGeoCoord(37.4822857823943,126.646410672519),
    MapPoint.mapPointWithGeoCoord(37.4634778848565,126.650001282535),
    MapPoint.mapPointWithGeoCoord(37.409493814977,126.678337878586),
    MapPoint.mapPointWithGeoCoord(37.4476151057576,126.733068368397),
    MapPoint.mapPointWithGeoCoord(37.5070683146994,126.721856264828),
    MapPoint.mapPointWithGeoCoord(37.5374405601649,126.737760072912),
    MapPoint.mapPointWithGeoCoord(37.4506797747895,126.69928849417),
    MapPoint.mapPointWithGeoCoord(37.5394836297326,126.734960628885),
    MapPoint.mapPointWithGeoCoord(37.544316773242,126.676503577483))

    val bokjiPoint = arrayOf(MapPoint.mapPointWithGeoCoord(37.545171523079,126.685770188126),
    MapPoint.mapPointWithGeoCoord(37.5117768004675,126.666278050191),
    MapPoint.mapPointWithGeoCoord(37.4617528000859,126.640659477075),
    MapPoint.mapPointWithGeoCoord(37.459575593541,126.645999172428),
    MapPoint.mapPointWithGeoCoord(37.5478266028925,126.725849880728))

    val sangdamPoint = arrayOf(MapPoint.mapPointWithGeoCoord(37.53515442101851,126.72290818723897),
        MapPoint.mapPointWithGeoCoord(37.46470039798029,126.71121597725626),
        MapPoint.mapPointWithGeoCoord(37.4830613330657,126.617699409983),
        MapPoint.mapPointWithGeoCoord(37.5148743484054,126.70453806158)
    )
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
    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
    }
    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
    }
    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
    }
    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
    }
    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewInitialized(p0: MapView?) {
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
    }
    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
        val url = "https://map.kakao.com/link/to/${p1?.itemName},${p1?.mapPoint?.mapPointGeoCoord?.latitude}, ${p1?.mapPoint?.mapPointGeoCoord?.longitude}"
        val nextIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        nextIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        Toast.makeText(this,"길찾기 페이지로 이동합니다",Toast.LENGTH_LONG).show()
        startActivity(context(),nextIntent,null)
    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
    }
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate = menuInflater
        inflate.inflate(R.menu.menu2,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
        R.id.first-> {
            val intent = intent
            val personname = intent.getStringExtra("name")
            menuCount("${item.title}",personname)
            val nextIntent = Intent(this,policy::class.java)
            startActivity(nextIntent)
        }
        R.id.second->{
            val nextIntent = Intent(this,money::class.java)
            startActivity(nextIntent)
        }
        R.id.third->{
            val intent = intent
            val name = intent.getStringExtra("name")
            val date = intent.getStringExtra("date")
            val sex = intent.getStringExtra("sex")
            val gohome = intent.getStringExtra("gohome")
            val year = intent.getStringExtra("year")
            val faceimg = intent.getStringExtra("Face")
            val qrimg = intent.getStringExtra("QR")
            val nextIntent = Intent(this,popup::class.java)
            nextIntent.putExtra("name",name)
            nextIntent.putExtra("date",date)
            nextIntent.putExtra("sex",sex)
            nextIntent.putExtra("gohome",gohome)
            nextIntent.putExtra("year",year)
            nextIntent.putExtra("QR",qrimg)
            nextIntent.putExtra("Face",faceimg)
            startActivity(nextIntent)
        }
        }
        return super.onOptionsItemSelected(item)
    }
    init {
        instance = this
    }
    companion object{
        private var instance: fragmap? = null
        fun context(): Context{
            return instance!!.applicationContext
        }
    }
    class CustomCalloutBalloonAdapter: CalloutBalloonAdapter {

        private val layoutInflater: LayoutInflater = context().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(KakaoApi.instance.base).addConverterFactory(
            GsonConverterFactory.create()).build()
        val networkService = retrofit.create(NetworkService::class.java)
        private var mCalloutBalloon: View
        init {
              mCalloutBalloon = layoutInflater.inflate(R.layout.custom_callout_balloon, null)
        }
        private val mmCalloutBalloon :LinearLayout = mCalloutBalloon.findViewById(R.id.custom_balloon)
        override fun getCalloutBalloon(poiItem: MapPOIItem): View{
            val address: Call<JsonObject> = networkService.keyword(
                "${KakaoApi.instance.key}",
                poiItem.itemName,
                poiItem.mapPoint.mapPointGeoCoord.longitude,
                poiItem.mapPoint.mapPointGeoCoord.latitude,
                "distance"
            )
            address.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val res = response.body()!!
                        Log.e("ㅌㅌ","$res")
                        val kakaoAdd = res.getAsJsonArray("documents")
                        val add_name = kakaoAdd.get(0)
                        val add = add_name.asJsonObject
                        val road = add.get("address_name").asString
                        val phone = add.get("phone").asString
                        if (phone == "" ){
                            (mmCalloutBalloon.findViewById(R.id.desc) as TextView).text = "전화번호: 정보가 없습니다."
                        }else {
                            (mmCalloutBalloon.findViewById(R.id.desc) as TextView).text = "전화번호: $phone"
                        }
                           (mmCalloutBalloon.findViewById(R.id.testView) as TextView).text = "주소: $road"
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("sign up fail", t.toString())
                }
            })
            Thread.sleep(200)
            (mmCalloutBalloon.findViewById(R.id.home_title) as TextView).text = poiItem.itemName

            return mmCalloutBalloon
        }
        override fun getPressedCalloutBalloon(poiItem: MapPOIItem): View? {
            return null
        }
    }
    class CustomCalloutBalloonAdapter2: CalloutBalloonAdapter {

        private val layoutInflater: LayoutInflater = context().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(KakaoApi.instance.base).addConverterFactory(
            GsonConverterFactory.create()).build()
        val retrofit2: Retrofit = Retrofit.Builder().baseUrl(ApplicationController.instance.baseURL).addConverterFactory(GsonConverterFactory.create()).client(
            OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor()).build()).build()

        val networkService = retrofit.create(NetworkService::class.java)
        val networkService2 = retrofit2.create(NetworkService::class.java)
        private var mCalloutBalloon: View
        init {
            mCalloutBalloon = layoutInflater.inflate(R.layout.custom_callout_balloon, null)
        }
        private val mmCalloutBalloon :LinearLayout = mCalloutBalloon.findViewById(R.id.custom_balloon)
        override fun getCalloutBalloon(poiItem: MapPOIItem): View{
            val address: Call<JsonObject> = networkService.address(
                "${KakaoApi.instance.key}",
                poiItem.mapPoint.mapPointGeoCoord.longitude,
                poiItem.mapPoint.mapPointGeoCoord.latitude
            )
            address.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val res = response.body()!!
                        val kakaoAdd = res.getAsJsonArray("documents")
                        Log.e("test1",kakaoAdd.toString())
                        val roadadd = kakaoAdd.asJsonArray.get(0)
                        var road = roadadd.asJsonObject.get("address_name")
                        if (road == null) {
                            road = roadadd.asJsonObject.get("address")
                            val test2 = JSONObject(road.toString()).getString("address_name")
                            (mmCalloutBalloon.findViewById(R.id.testView)as TextView).text = "주소: $test2"

                        } else {
                            val test = JSONObject(road.toString()).getString("address_name")
                            (mmCalloutBalloon.findViewById(R.id.testView)as TextView).text = "주소: $test"
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("sign up fail", t.toString())
                }
            })
            val count : Call<String> = networkService2.food(poiItem.itemName.toString())
            count.enqueue(object : Callback<String>{

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val food = response.body()
                    Log.e("food", "$food")
                    (mmCalloutBalloon.findViewById(R.id.desc) as TextView).text = "남은 밥 양은 ${food}인분 입니다"
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("fail", t.toString())
                }
            })
            Thread.sleep(900)
            (mmCalloutBalloon.findViewById(R.id.home_title) as TextView).text = poiItem.itemName
            return mmCalloutBalloon
        }
        override fun getPressedCalloutBalloon(poiItem: MapPOIItem): View? {
            return null
        }
    }
    class CustomCalloutBalloonAdapter3: CalloutBalloonAdapter {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(KakaoApi.instance.base).addConverterFactory(
            GsonConverterFactory.create()).build()
        val networkService = retrofit.create(NetworkService::class.java)
        private val layoutInflater: LayoutInflater = context().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        private var mCalloutBalloon: View
        init {
            mCalloutBalloon = layoutInflater.inflate(R.layout.custom_callout_balloon, null)
        }
        private val mmCalloutBalloon :LinearLayout = mCalloutBalloon.findViewById(R.id.custom_balloon)
        override fun getCalloutBalloon(poiItem: MapPOIItem): View{
            val address: Call<JsonObject> = networkService.address(
                "${KakaoApi.instance.key}",
                poiItem.mapPoint.mapPointGeoCoord.longitude,
                poiItem.mapPoint.mapPointGeoCoord.latitude
            )
            address.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.isSuccessful) {
                        val res = response.body()!!
                        val kakaoAdd = res.getAsJsonArray("documents")
                        Log.e("test1",kakaoAdd.toString())
                        val road_add = kakaoAdd.asJsonArray.get(0)
                        var road = road_add.asJsonObject.get("address_name")
                        if (road == null) {
                            road = road_add.asJsonObject.get("address")
                            val test2 = JSONObject(road.toString()).getString("address_name")
                            (mmCalloutBalloon.findViewById(R.id.testView)as TextView).text = "주소: $test2"

                        } else {
                            val test = JSONObject(road.toString()).getString("address_name")
                            (mmCalloutBalloon.findViewById(R.id.testView)as TextView).text = "주소: $test"
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("sign up fail", t.toString())
                }
            })

            Thread.sleep(200)
            (mmCalloutBalloon.findViewById(R.id.home_title) as TextView).text = poiItem.itemName
            (mmCalloutBalloon.findViewById(R.id.desc) as TextView).text = "전화번호: 정보가 없습니다"

            return mmCalloutBalloon
        }
        override fun getPressedCalloutBalloon(poiItem: MapPOIItem): View? {
            return null
        }
    }
    var token: String? = null
    fun menuCount(menuName: String, Name: String){
        val networkService : NetworkService by lazy {
            ApplicationController.instance.networkService
        }
        val obj = JSONObject()
        obj.put("name" ,Name)
        obj.put("token", token)
        obj.put("function",menuName)
        val json = obj.toString()
        val gsonObject = JsonParser().parse(json) as JsonObject
        val menucount: Call<JsonObject> = networkService.menuCount(gsonObject)
        menucount.enqueue(object :Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(activity_fragmap)
        val bott : BottomNavigationView = findViewById(R.id.nav_view)
        val mapView = MapView(this)
        mapView.setMapViewEventListener(this)
        mapView.setPOIItemEventListener(this)
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),100)
        }
        val intent = intent
        var personname = intent.getStringExtra("name")
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener{
                task->
            if (!task.isSuccessful){
                Log.w( "getInstanceId failed", task.exception)
                return@OnCompleteListener
            }
            token = task.result?.token
            Log.e("토큰", token)
        })
        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        var latitude = location.latitude
        var longitude = location.longitude
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 2,true)
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
        mapView.setShowCurrentLocationMarker(true)
        mapView.setCurrentLocationRadius(100)
        mapView.mapType = MapView.MapType.Hybrid
        val container = findViewById<FrameLayout>(R.id.nav_host_fragment)
        container.addView(mapView)
        val loc = findViewById<Button>(R.id.location)
        loc.setOnClickListener {
            mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 2,true)
            mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
        }
        class ItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
            @SuppressLint("ResourceType")
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.navigation_freefood -> {
                        menuCount("${menuItem.title}",personname)
                        mapView.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter2())
                        mapView.removeAllPOIItems()
                        for (i in 0..array2.size-1) {
                            var marker = MapPOIItem()
                    //        marker.itemName = map3[i]
                            marker.itemName = resources.getStringArray(R.array.freefoodname)[i].toString()
                            marker.mapPoint = array2[i]
                            marker.tag = i
                            marker.showAnimationType = MapPOIItem.ShowAnimationType.SpringFromGround
                            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                            marker.isShowCalloutBalloonOnTouch = true
                            marker.isShowDisclosureButtonOnCalloutBalloon = true
                            mapView.addPOIItem(marker)
                        }
                    }
                    R.id.navigation_dashboard -> {

                       // menuCount("${menuItem.title}",personname)
                        mapView.removeAllPOIItems()
                        mapView.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter())
                        Toast.makeText(this@fragmap,"결과는 반경5Km 이내, 최대 45개까지 표시됩니다.", Toast.LENGTH_LONG).show()
                        val retrofit: Retrofit = Retrofit.Builder().baseUrl(KakaoApi.instance.base).addConverterFactory(
                            GsonConverterFactory.create()).build()
                        val networkService = retrofit.create(NetworkService::class.java)
                        for(i in 1.. 3){
                            val address: Call<JsonObject> = networkService.keywordjob("${KakaoApi.instance.key}", "병원",
                                location.longitude,
                                location.latitude,
                                5000,
                                "distance",
                                i
                            )
                            address.enqueue(object : Callback<JsonObject> {
                                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                    if (response.isSuccessful) {
                                        val keyword = response.body()!!
                                        val jobkey = keyword.get("documents")
                                        for ( i in 0 .. 14) {
                                            val jobkeyword = jobkey.asJsonArray.get(i)
                                            val jobname = jobkeyword.asJsonObject.get("place_name")
                                            val jobx = jobkeyword.asJsonObject.get("y")
                                            val joby = jobkeyword.asJsonObject.get("x")
                                            val marker = MapPOIItem()
                                            marker.itemName = jobname.asString
                                            marker.tag = i
                                            marker.showAnimationType = MapPOIItem.ShowAnimationType.SpringFromGround
                                            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                                            marker.isShowCalloutBalloonOnTouch = true
                                            marker.isShowDisclosureButtonOnCalloutBalloon = true
                                            marker.mapPoint = MapPoint.mapPointWithGeoCoord(jobx.asDouble,joby.asDouble)
                                            mapView.addPOIItem(marker)
                                        }
                                    }
                                }
                                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                    Log.e("sign up fail", t.toString())
                                }
                            })
                        }

                    }
                    R.id.navigation_notifications -> {
                        menuCount("${menuItem.title}",personname)
                        mapView.removeAllPOIItems()
                        mapView.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter())
                        for (i in 0.. sangdamPoint.size-1 ){
                            var marker = MapPOIItem()
                            marker.itemName = resources.getStringArray(R.array.sangdam)[i].toString()
                            marker.mapPoint = sangdamPoint[i]
                            marker.showAnimationType = MapPOIItem.ShowAnimationType.SpringFromGround
                            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                            marker.isShowCalloutBalloonOnTouch = true
                            marker.isShowDisclosureButtonOnCalloutBalloon = true
                            mapView.addPOIItem(marker)
                        }
                        mapView.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter3())
                        for (i in 0..1){
                            var marker = MapPOIItem()
                            marker.itemName = sangdam[i]
                            marker.mapPoint = sangdam2[i]
                            marker.showAnimationType = MapPOIItem.ShowAnimationType.SpringFromGround
                            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                            marker.isShowCalloutBalloonOnTouch = true
                            marker.isShowDisclosureButtonOnCalloutBalloon = true
                            mapView.addPOIItem(marker)
                        }
                    }
                    R.id.navigation_home -> {
                        menuCount("${menuItem.title}",personname)
                        mapView.removeAllPOIItems()
                        mapView.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter3())
                        for (i in 0..bokjiPoint.size-1){
                            var marker = MapPOIItem()
                            marker.itemName = resources.getStringArray(R.array.bokji)[i].toString()
                            marker.mapPoint = bokjiPoint[i]
                            marker.showAnimationType = MapPOIItem.ShowAnimationType.SpringFromGround
                            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                            marker.isShowCalloutBalloonOnTouch = true
                            marker.isShowDisclosureButtonOnCalloutBalloon = true
                            mapView.addPOIItem(marker)
                        }
                        Log.e("ㅅㅁㄴㄻㄴ","$longitude , $latitude")
                    }
                    R.id.navigation_job->{

                       // menuCount("${menuItem.title}",personname)
                        mapView.removeAllPOIItems()
                        mapView.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter3())
                        for (i in 0..jobpoint.size-1){
                            var marker = MapPOIItem()
                            marker.itemName = resources.getStringArray(R.array.jobname)[i].toString()
                            marker.mapPoint = jobpoint[i]
                            marker.showAnimationType = MapPOIItem.ShowAnimationType.SpringFromGround
                            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                            marker.isShowCalloutBalloonOnTouch = true
                            marker.isShowDisclosureButtonOnCalloutBalloon = true
                            mapView.addPOIItem(marker)
                        }

                    }
                }
                return true
            }
        }
        bott.setOnNavigationItemSelectedListener(ItemSelectedListener())
    }
}