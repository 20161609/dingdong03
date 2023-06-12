package com.example.dingdong3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class splahActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splah)
        val intent : Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}