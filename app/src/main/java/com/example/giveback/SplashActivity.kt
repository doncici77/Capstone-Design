package com.example.giveback

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.example.giveback.auth.LoginActivity

// 앱을 실행했을 때 3초간 실행되게 할 스플래쉬 페이지
class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 현재 로그인한 유저가 없거나 이메일 인증이 되지 않았다면, 로그인 화면으로 이동
        val user = auth.currentUser
        Handler().postDelayed({
            if (user == null || !user.isEmailVerified) {
                // 유저가 없거나 이메일 인증이 안된 경우
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                // 유저가 있고 이메일 인증도 된 경우
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }, 3000)
    }
}