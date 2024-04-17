package com.example.giveback.GetBoard

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.giveback.R
import com.example.giveback.comment.CommentLVAdpater
import com.example.giveback.comment.CommentModel
import com.example.giveback.databinding.ActivityGetBoardInsideBinding
import com.example.giveback.utils.FBAuth
import com.example.giveback.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// 게시글 보기 페이지
class GetBoardInsideActivity : AppCompatActivity() {

    private val TAG = GetBoardInsideActivity::class.java.simpleName

    private lateinit var binding: ActivityGetBoardInsideBinding

    private lateinit var key:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_board_inside)

        // boardSettingIcon을 클릭하면 custom_dialog layout이 나오도록 이벤트 처리
        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        binding.manageNumber.text = "관리번호 : ${key}"
    }

    // 만든 custom_dialog를 띄우는 showDialog() 함수 생성
    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        // 수정버튼을 클릭했을 때
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this, "수정 버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, GetBoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)

            alertDialog.dismiss()
        }
        // 삭제버튼을 클릭했을 때
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {

            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this)
                .setIcon(R.drawable.chat)
                .setTitle("해당 게시글을 삭제하겠습니까?")
                .setPositiveButton("확인") { dialog, which ->
                    FBRef.getboardRef.child(key).removeValue()
                    Toast.makeText(this, "삭제완료", Toast.LENGTH_SHORT).show()

                    finish()
                }
                .setNegativeButton("취소", null)
                .create()
            alertDialog.show()
        }
    }
    // 이미지 데이터를 받아오는 함수
    private fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child(key + ".png")

        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {
                binding.getImageArea.isVisible = false
            }
        })
    }

    // 게시글을 가져오는 함수
    private fun getBoardData(key: String) {
        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(GetBoardModel::class.java)

                if (dataModel != null) {

                    binding.emailArea.setText("습득자: ${dataModel.email}")
                    binding.titleArea.setText("습득물명: ${dataModel.title}")
                    binding.categoryArea.setText("카테고리명: ${dataModel.category}")
                    binding.getDateArea.setText("습득날짜: ${dataModel.getDate}")
                    binding.getlocationArea.setText("습득위치: ${dataModel.getLocation} ${dataModel.getdetailLocation}")
                    binding.keeplocationArea.setText("보관위치: ${dataModel.keepLocation} ${dataModel.keepdetailLocation}")
                    binding.contentArea.setText("상세내용 : ${dataModel.content}")
                }

                val myUid = FBAuth.getUid()
                val writerUid = dataModel?.uid
                if(myUid.equals(writerUid)){
                    binding.boardSettingIcon.isVisible = true
                } else {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.getboardRef.child(key).addValueEventListener(postListner)
    }
}