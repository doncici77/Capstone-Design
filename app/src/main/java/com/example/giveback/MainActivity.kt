package com.example.giveback

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

// 메인 화면
class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val alertDialog = AlertDialog.Builder(this)
            .setIcon(R.drawable.chat)
            .setTitle("종료")
            .setMessage("정말로 종료하시겠습니까?")
            .setPositiveButton("확인") { dialog, which ->
                finish()
            }
            .setNegativeButton("취소", null)
            .create()
        alertDialog.show()
    }
}