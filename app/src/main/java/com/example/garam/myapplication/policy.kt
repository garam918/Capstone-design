package com.example.garam.myapplication

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

class policy : AppCompatActivity() {

    var lists = arrayListOf<polList>()
    private var backKeyPressedTime: Long = 0

    @SuppressLint("ResourceType")
    private fun run(){
        for (j in 1 .. 9) {
            val xml: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse("http://api.korea.go.kr/openapi/svc/list?lrgAstCd=&mdmAstCd=&smallAstCd=&jrsdOrgCd=&srhQuery=%EB%85%B8%EC%88%99&pageSize=100&format=xml&serviceKey=GQOMc1HbWO7YPxXTvHmKsEcMapeqMjtBbxH8heT31xs9hJgfWWa8Q6y8gLBadFds6ZLL7V9D3ZBJPu9F%2FJE3%2Bg%3D%3D&pageIndex=$j")
            xml.documentElement.normalize()
            var policyList: String? = null
            var polName: String? = null
            var polUrl: String? = null
            val list = xml.getElementsByTagName("svc")
            //   val fragment1 : Fragment = BlankFragment()
            //  val bundle = Bundle(1)
            for (i in 0 until list.length) {
                var n: Node = list.item(i)
                if (n.nodeType == Node.ELEMENT_NODE) {
                    val elem = n as Element

                    val map = mutableMapOf<String, String>()
                    for (j in 0 until elem.attributes.length - 1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            map.putIfAbsent(
                                elem.attributes.item(j).nodeName,
                                elem.attributes.item(j).nodeValue
                            )
                        }
                    }
                    if ("${elem.getElementsByTagName("jrsdDptAllNm")
                            .item(0).textContent.commonPrefixWith("인천")}" == "인천"
                    ) {
                        Log.e("i 값", "=======${i + 1}=========")
                        Log.e(
                            "1. 서비스 이름 ",
                            "${elem.getElementsByTagName("svcNm").item(0).textContent}"
                        )
                        Log.e(
                            "2. 기관 이름 ",
                            "${elem.getElementsByTagName("jrsdDptAllNm").item(0).textContent}"
                        )
                        Log.e(
                            "3. URL 주소 ",
                            "${elem.getElementsByTagName("svcInfoUrl").item(0).textContent}"
                        )
                        //       bundle.putString("url","${elem.getElementsByTagName("svcInfoUrl").item(0).textContent}")
                        policyList = elem.getElementsByTagName("svcNm").item(0).textContent
                        polName = elem.getElementsByTagName("jrsdDptAllNm").item(0).textContent
                        polUrl = elem.getElementsByTagName("svcInfoUrl").item(0).textContent
                        //      lists.add(polList("${elem.getElementsByTagName("jrsdDptAllNm").item(0).textContent}","${elem.getElementsByTagName("svcNm").item(0).textContent}"))
                        lists.add(polList(policyList, polName, polUrl))
                        Log.e("polList", lists.toString())
                        // lists.add("${elem.getElementsByTagName("jrsdDptAllNm").item(0).textContent}","${elem.getElementsByTagName("svcNm").item(0).textContent}")
                        //fragment1.arguments = bundle
                        ///fragmentTransaction.commit()
                    }
                }
            }
            //  val fragmentTransaction = supportFragmentManager.beginTransaction()
            //   fragmentTransaction.replace(R.id.WebView, fragment1).commit()
        }
    }
    lateinit var webView: WebView
    lateinit var recycler : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)
        val thread = Thread{
            run()
        }
        thread.start()
        webView = findViewById<WebView>(R.id.WebView)
        webView.webViewClient = WebViewClient()
        var mWebsetting = webView.settings
        mWebsetting.javaScriptEnabled = true
        mWebsetting.setSupportMultipleWindows(false)
        mWebsetting.javaScriptCanOpenWindowsAutomatically = false
        mWebsetting.useWideViewPort = true
        mWebsetting.loadWithOverviewMode = true
        mWebsetting.setSupportZoom(true)
        mWebsetting.builtInZoomControls = true
        mWebsetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        mWebsetting.cacheMode = WebSettings.LOAD_NO_CACHE
        val spinner1 = findViewById<Spinner>(R.id.spinner)
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        val adapter1 = ArrayAdapter.createFromResource(this,R.array.서비스분류,android.R.layout.simple_spinner_item)
        val adapter2 = ArrayAdapter.createFromResource(this,R.array.지역별,android.R.layout.simple_spinner_item)
        spinner1.adapter = adapter1
        spinner2.adapter = adapter2
        //run()
        recycler = findViewById<RecyclerView>(R.id.recyclerView)
        Thread.sleep(1000)
        val test = StdRecyclerAdapter(lists,this){
            polList ->
            recycler.visibility = View.INVISIBLE
            webView.visibility = View.VISIBLE
            webView.loadUrl(polList.url)
            Toast.makeText(this,"${polList.url}",Toast.LENGTH_LONG).show()
            //Toast.makeText(this,"정책이름 : ${polList.name} , 기관  ${polList.list}",Toast.LENGTH_LONG).show()
        }
        recycler.adapter = test
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               when(position){
                   1 -> webView.loadUrl("http://api.korea.go.kr/openapi/svc/list?lrgAstCd=&mdmAstCd=&smallAstCd=&jrsdOrgCd=6110000&srhQuery=%EB%85%B8%EC%88%99%EC%9D%B8&DATE&pageIndex=1&pageSize=5&format=html&serviceKey=정부24API")
                   2 -> webView.loadUrl("http://api.korea.go.kr/openapi/svc/list?lrgAstCd=&mdmAstCd=&smallAstCd=&jrsdOrgCd=&srhQuery=%EB%85%B8%EC%88%99%EC%9D%B8&DATE&pageIndex=1&pageSize=5&format=html&serviceKey=정부24API")
               }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        }
      override fun onBackPressed() {
          lateinit var toast: Toast
       //   val webView = findViewById<WebView>(R.id.WebView)
      //    val recycler = findViewById<RecyclerView>(R.id.recyclerView)
           if (System.currentTimeMillis() >= backKeyPressedTime + 1500) {
               backKeyPressedTime = System.currentTimeMillis()
               toast = Toast.makeText(this, "종료 하려면 한번 더 누르세요.", Toast.LENGTH_LONG)
               toast.show()
              // webView.destroy()
               webView.visibility = View.INVISIBLE
               recycler.visibility = View.VISIBLE
               return
           }
           if (System.currentTimeMillis() < backKeyPressedTime + 1500) {
               finish()
           }

    }
}
