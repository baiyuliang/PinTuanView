package com.byl.pin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.byl.pin.view.PinTuanView

class MainActivity : AppCompatActivity() {

    private lateinit var mPinTuanView: PinTuanView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPinTuanView = findViewById(R.id.mPinTuanView)
        mPinTuanView.setData()
        mPinTuanView.start()

    }

    override fun onDestroy() {
        super.onDestroy()
        mPinTuanView.stop()
    }
}