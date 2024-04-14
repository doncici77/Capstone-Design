package com.example.giveback.auth

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.giveback.MainActivity
import com.example.giveback.R
import com.example.giveback.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth

// 회원가입 페이지
class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityJoinBinding

    private var email_ok: Boolean = false
    private var pw_ok: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        // 회원가입 버튼 초기 상태는 비활성화
        binding.joinBtn.isEnabled = false

        // 모든 필드 입력 되면 가입하기 버튼 활성화
        // 1. 이메일 필드
        binding.emailArea.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                email_ok = email.endsWith("@gwnu.ac.kr") || email.endsWith("@gwnu.mypiug.kr")
                if (!email.contains('@')) {
                    // 이메일에 '@'가 포함되어 있는지 확인
                    binding.emailokText.setTextColor(Color.RED)
                    binding.emailokText.text = "이메일 형식에 맞게 작성하세요."
                } else if (!email_ok) {
                    // 지정된 도메인 확인
                    binding.emailokText.setTextColor(Color.RED)
                    binding.emailokText.text = "gwnu.ac.kr 혹은 gwnu.mypiug.kr 도메인으로 입력하세요."
                } else {
                    binding.emailokText.setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
                    binding.emailokText.text = "유효한 아이디"
                }
                changeSignUpButtonActivation()
            }
        })

        // 2. 비밀번호 필드
        binding.passwordArea1.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pw_ok = s?.length!! >= 6 && checkPassword() // 6자 이상 + 비밀번호 입력 정상
                if (s?.length!! >= 6) {
                    binding.pwokText.setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
                    binding.pwokText.text = "유효한 비밀번호"
                } else {
                    binding.pwokText.setTextColor(Color.RED)
                    binding.pwokText.text = "비밀번호는 6자 이상이어야 합니다"
                }
                changeSignUpButtonActivation()
            }
        })

        // 4. 비밀번호 확인 필드
        binding.passwordArea2.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pw_ok = s?.length!! >= 6 && checkPassword() // 6자 이상 + 비밀번호 입력 정상
                if (pw_ok) {
                    binding.pwcheckokText.setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
                    binding.pwcheckokText.text = "유효한 비밀번호 확인"
                } else {
                    binding.pwcheckokText.setTextColor(Color.RED)
                    binding.pwcheckokText.text = "비밀번호가 위와 같아야 합니다"
                }
                changeSignUpButtonActivation()
            }
        })

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

    // 이메일 인증 버튼 활성화 상태 변경 메소드
    fun changeSignUpButtonActivation() : Unit {
        binding.verifyEmailBtn.isEnabled =  email_ok == true && pw_ok == true
    }

    // 비밀번호 == 비밀번호 확인이면 true, 아니면 false
    fun checkPassword() : Boolean {
        return binding.passwordArea1.text.toString() == binding.passwordArea2.text.toString()
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
