package com.demoreact

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.demoreact.databinding.ActivityMainBinding
import com.facebook.react.ReactActivity
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
    }

    fun initViews(){
        binding.btnStartReactNative.setOnClickListener {
            val intent = Intent(this, ReactMainActivity::class.java)
            startActivity(intent)
        }
        binding.btnStartFlutterMain.setOnClickListener {
            val flutterIntent = MyFlutterActivity
                .withCachedEngine("flutter_engine_main")
                .build(this)

            // Add data via intent extras
            flutterIntent.putExtra("userId", "12345")
            flutterIntent.putExtra("userName", "John Doe ${Date()}")
            flutterIntent.putExtra("complexData", "{\"key\":\"value\"}")
            flutterIntent.putExtra("routeName", "/")

            startActivity(flutterIntent)
        }
        binding.btnStartFlutterSecond.setOnClickListener {
            val flutterIntent = MyFlutterActivity
                .withCachedEngine("flutter_engine_main")
                .build(this)

            flutterIntent.putExtra("routeName", "/second")
            startActivity(flutterIntent)
        }
    }
}