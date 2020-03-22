package com.example.garam.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.squti.androidwaverecorder.WaveRecorder

class AudioRecord : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_record)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 100)
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }
        Toast.makeText(this,"버튼을 누르고 화면에 표시된 글자를 읽어주세요",Toast.LENGTH_LONG).show()
        val recordbutton = findViewById<Button>(R.id.button4)
        val okbutton = findViewById<Button>(R.id.button5)
        val recorder = MediaRecorder()
        val path = cacheDir.path + "/" + "recorder.wav"
        val waveRecorder = WaveRecorder(path)

        recordbutton.setOnClickListener {
            /*val path = cacheDir.path + "/" + "recorder.mp3"
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            recorder.setOutputFile(path)
            recorder.prepare()
            recorder.start() */
            waveRecorder.waveConfig.sampleRate = 44100
            waveRecorder.waveConfig.channels = AudioFormat.CHANNEL_IN_STEREO
            waveRecorder.waveConfig.audioEncoding = AudioFormat.ENCODING_PCM_8BIT
            waveRecorder.startRecording()
        }
        okbutton.setOnClickListener {
            Toast.makeText(this,"녹음 완료",Toast.LENGTH_LONG).show()
            /*recorder.stop()
            recorder.release()
            finish()*/
            waveRecorder.stopRecording()
            finish()
        }
    }


}
