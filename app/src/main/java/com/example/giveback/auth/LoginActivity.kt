package com.example.giveback.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.giveback.MainActivity
import com.example.giveback.R
import com.example.giveback.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

// 로그인 페이지
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // 회원가입 버튼을 눌렀을 때 회원가입 페이지로 이동
        binding.joinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            var email = binding.emailArea.text.toString()
            var password = binding.passwordArea.text.toString()

            // 이메일과 비밀번호가 비어있는지 확인
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 로그인에 성공하면 로그인 성공 메시지와 함께 메인 페이지로 이동, 실패하면 실패 메시지만 출력
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 로그인 성공 시 처리
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_LONG).show()
                    } else {
                        // 로그인 실패 시 처리
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}