package com.example.giveback.LostBoard

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.giveback.R
import com.example.giveback.databinding.ActivityLostBoardWriteBinding
import com.example.giveback.utils.FBAuth
import com.example.giveback.utils.FBRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

// 게시글 작성 페이지
class LostBoardWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLostBoardWriteBinding

    private var isImageUpload = false
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_lost_board_write)

        // 게시글 작성 버튼을 눌렀을 때 파이어베이스에 게시글과 이미지를 넣는다.
        binding.writeBtn.setOnClickListener {

            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()

            // uid를 가져온다.
            val uid = FBAuth.getUid()

            // time을 가져온다.
            val time = FBAuth.getTime()

            // 키부터 생성하고 데이터베이스에 저장하도록 수정
            val key = FBRef.lostboardRef.push().key.toString()

            // 파이어 베이스에 데이터를 저장한다.
            FBRef.lostboardRef
                .child(key) // 랜덤한 값
                .setValue(LostBoardModel(title,content,uid,time))

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