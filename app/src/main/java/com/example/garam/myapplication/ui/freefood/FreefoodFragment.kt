package com.example.garam.myapplication.ui.freefood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.garam.myapplication.R
import com.example.garam.myapplication.fragmap
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class FreefoodFragment : Fragment() {

    private lateinit var freefoodViewModel: FreefoodViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        freefoodViewModel =
            ViewModelProviders.of(this).get(FreefoodViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_freefood, container, false)
        val textView: TextView = root.findViewById(R.id.text_freefood)
        freefoodViewModel.text.observe(this, Observer {
            textView.text = it
        })

        return root
    }
}