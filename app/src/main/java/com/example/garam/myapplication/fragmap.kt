package com.example.garam.myapplication

import android.Manifest
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
import com.example.garam.myapplication.R.layout.activity_fragmap
import com.example.garam.myapplication.network.ApplicationController
import com.example.garam.myapplication.network.KakaoApi
import com.example.garam.myapplication.network.NetworkService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
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
    val array = arrayOf(MapPoint.mapPointWithGeoCoord(37.449977, 126.738597),
        MapPoint.mapPointWithGeoCoord(37.463504, 126.725468),
        MapPoint.mapPointWithGeoCoord(37.447762, 126.736950),
        MapPoint.mapPointWithGeoCoord(37.465845, 126.697053),
        MapPoint.mapPointWithGeoCoord(37.450973, 126.697235),
        MapPoint.mapPointWithGeoCoord(37.448230, 126.717807),
        MapPoint.mapPointWithGeoCoord(37.405087, 126.736721),
        MapPoint.mapPointWithGeoCoord(37.454358, 126.724774))
    val map2 :Map<Int,String> = mapOf( 0 to "만수종합사회복지관",
        1 to "만월종합사회복지관",
        2 to "남동장애인종합복지관",
        3 to "성산종합사회복지관",
        4 to "남동구 노인복지관",
        5 to "대흥교회",
        6 to "논현감리교회",
        7 to "송이무료급식소")
    val sangdam: Map<Int,String> = mapOf(0 to "창신동쪽방상담센터",
            1 to "남대문쪽방상담소",
            2 to "돈의동쪽방상담소",
            3 to "서울역쪽방상담소",
            4 to "영등포쪽방상담소",
            5 to "동구쪽방상담소",
            6 to "부산진구쪽방상담소",
            7 to "대구쪽방상담소",
            8 to "인천내일을여는집 쪽방상담소",
            9 to "대전광역시쪽방상담소")
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
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.navigation_freefood -> {
                        mapView.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter2())
                        mapView.removeAllPOIItems()
                        for (i in 0..7) {
                            var marker = MapPOIItem()
                            marker.itemName = map2[i]
                            marker.mapPoint = array[i]
                            marker.tag = i
                            marker.showAnimationType = MapPOIItem.ShowAnimationType.SpringFromGround
                            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                            marker.isShowCalloutBalloonOnTouch = true
                            marker.isShowDisclosureButtonOnCalloutBalloon = true
                            mapView.addPOIItem(marker)
                        }
                    }
                    R.id.navigation_dashboard -> {
                        mapView.removeAllPOIItems()
                        mapView.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter())
                        val retrofit: Retrofit = Retrofit.Builder().baseUrl(ApplicationController.instance.baseURL).addConverterFactory(
                            GsonConverterFactory.create()).build()
                        val networkService = retrofit.create(NetworkService::class.java)
                        val Coffee: Call<JsonArray> = networkService.coffee(
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                        Coffee.enqueue(object : Callback<JsonArray>{
                            override fun onResponse(
                                call: Call<JsonArray>,
                                response: Response<JsonArray>
                            ) {
                                Log.e("성공","${response.body()}")
                            }
                            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                                Log.e("실패",": 실패함")
                            }
                        })
                    }
                    R.id.navigation_notifications -> {
                        mapView.removeAllPOIItems()
                        mapView.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter())
                    }
                    R.id.navigation_home -> {
                        mapView.removeAllPOIItems()
                        mapView.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter())
                        Log.e("ㅅㅁㄴㄻㄴ","$longitude , $latitude")
                    }
                    R.id.navigation_job->{
                        mapView.removeAllPOIItems()
                        mapView.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter())
                        Toast.makeText(this@fragmap,"결과는 반경5Km 이내, 최대 45개까지 표시됩니다.", Toast.LENGTH_LONG).show()
                        val retrofit: Retrofit = Retrofit.Builder().baseUrl(KakaoApi.instance.base).addConverterFactory(
                            GsonConverterFactory.create()).build()
                        val networkService = retrofit.create(NetworkService::class.java)
                        for(i in 1.. 3){
                        val address: Call<JsonObject> = networkService.keywordjob("${KakaoApi.instance.key}", "인력사무소",
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
                }
                return true
            }
        }
        bott.setOnNavigationItemSelectedListener(ItemSelectedListener())
    }
}