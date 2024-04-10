package com.example.giveback.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.giveback.MainActivity
import com.example.giveback.R
import com.example.giveback.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth

// 회원가입 페이지
class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        // 회원가입 버튼 초기 상태는 비활성화
        binding.joinBtn.isEnabled = false

        // 이메일 인증 버튼 클릭 리스너
        binding.verifyEmailBtn.setOnClickListener {
            val email = binding.emailArea.text.toString()
            val password1 = binding.passwordArea1.text.toString()
            val password2 = binding.passwordArea2.text.toString()

            // 이메일, 비밀번호, 비밀번호 확인 입력 검증
            if (email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
                Toast.makeText(this, "이메일, 비밀번호, 비밀번호 확인을 모두 입력해주세요.", Toast.LENGTH_LONG).show()
            } //else if (!email.endsWith("@gwnu.ac.kr")) { Toast.makeText(this, "gwnu.ac.kr 도메인의 이메일을 사용해주세요.", Toast.LENGTH_LONG).show() } 인증 메일이 안옴
            else if (password1 != password2) {
                Toast.makeText(this, "비밀번호가 서로 일치하지 않습니다.", Toast.LENGTH_LONG).show()
            } else if (password1.length < 6) {
                Toast.makeText(this, "비밀번호를 6자 이상으로 설정해주세요.", Toast.LENGTH_LONG).show()
            } else {
                createUserAndSendEmailVerification(email, password1)
            }
        }

        binding.joinBtn.setOnClickListener {
            // 회원가입 버튼 클릭 시의 액션은 이메일 인증 확인 후에 활성화되어야 함
            // 여기서는 예제 코드로 바로 메인 액티비티로 이동하도록 설정
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUserAndSendEmailVerification(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                    if (verificationTask.isSuccessful) {
                        Toast.makeText(this, "인증 이메일이 발송되었습니다. 이메일을 확인해주세요.", Toast.LENGTH_LONG).show()
                        // 이메일 인증 후에 회원가입 버튼 활성화
                        binding.joinBtn.isEnabled = true
                    } else {
                        Toast.makeText(this, "이메일 발송에 실패했습니다.", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "회원가입 실패", Toast.LENGTH_LONG).show()
            }
        }
    }
}
