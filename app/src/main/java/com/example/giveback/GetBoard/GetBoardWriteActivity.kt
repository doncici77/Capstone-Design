package com.example.giveback.GetBoard

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.giveback.R
import com.example.giveback.WebviewActivity
import com.example.giveback.databinding.ActivityGetBoardWriteBinding
import com.example.giveback.utils.FBAuth
import com.example.giveback.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.Calendar

// 게시글 작성 페이지
class GetBoardWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGetBoardWriteBinding

    private var isImageUpload = false

    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email.toString()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_get_board_write)

        // 습득 위치 옆의 물음표 버튼을 눌렀을 때 학교 건물 웹뷰로 이동한다.
        binding.question.setOnClickListener {
            val intent = Intent(this, WebviewActivity::class.java)
            startActivity(intent)
        }

        // 날짜를 입력받을 때 달력이 나오고 달력에서 날짜를 선택하면 선택한 날짜가 text로 들어간다.
        val datePickerListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            // 선택한 날짜를 원하는 형식으로 텍스트로 변환
            val selectedDateText = "${year}년 ${month + 1}월 ${dayOfMonth}일"

            // 버튼의 텍스트를 선택한 날짜로 변경
            binding.getDateArea.text = selectedDateText
        }

        // 버튼 클릭 시 DatePickerDialog를 띄우는 코드
        binding.getDateArea.setOnClickListener {
            val calendar = Calendar.getInstance()
            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

            // DatePickerDialog 생성
            val datePickerDialog = DatePickerDialog(this, datePickerListener, initialYear, initialMonth, initialDay)
            datePickerDialog.show()
        }

        // 게시글 작성 버튼을 눌렀을 때 파이어베이스에 게시글과 이미지를 넣는다.
        binding.writeBtn.setOnClickListener {

            // uid를 가져온다.
            val uid = FBAuth.getUid()

            val getUid = uid.toString()
            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val getDate = binding.getDateArea.text.toString()
            val getLocation = binding.getlocationArea.text.toString()
            val keepLocation = binding.keeplocationArea.text.toString()

            // 키부터 생성하고 데이터베이스에 저장하도록 수정
            val key = FBRef.getboardRef.push().key.toString()

            // 파이어 베이스에 데이터를 저장한다.
            FBRef.getboardRef
                .child(key) // 랜덤한 값
                .setValue(GetBoardModel(uid, email, title, getDate, getLocation, keepLocation, content,))

            Toast.makeText(this,"게시글 입력 완료", Toast.LENGTH_LONG).show()

            // 이미지를 Firebase 스토리지에 업로드
            if(isImageUpload) {
                imageUpload(key)
            }

            finish()
        }
        // 이미지 영역을 클릭했을 때 이미지를 업로드한다.
        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }
    }

    // 이미지를 업로드하는 함수
    private fun imageUpload(key: String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key + ".png")

        val imageView = binding.imageArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()

        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.imageArea.setImageURI(data?.data)

        }
    }
}