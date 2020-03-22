package com.example.garam.myapplication.ui.job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.garam.myapplication.R
import kotlinx.android.synthetic.main.fragment_job.*

class JobFragment : Fragment() {

    private lateinit var jobViewModel: JobViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        jobViewModel =
            ViewModelProviders.of(this).get(JobViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_job, container, false)
        val textView: TextView = root.findViewById(R.id.text_job)
        jobViewModel.text.observe(this, Observer {
            textView.text = it
    //        qrFromServer.setImageBitmap()
        })
        return root
    }
}
