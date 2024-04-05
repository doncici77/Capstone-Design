package com.example.giveback.LostBoard

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
import com.example.giveback.databinding.ActivityLostBoardInsideBinding
import com.example.giveback.utils.FBAuth
import com.example.giveback.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// 게시글 보기 페이지
class LostGetBoardInsideActivity : AppCompatActivity() {

    private val TAG = LostGetBoardInsideActivity::class.java.simpleName

    private lateinit var binding: ActivityLostBoardInsideBinding

    private lateinit var key:String

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentAdapter : CommentLVAdpater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_board_inside)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_lost_board_inside)

        // boardSettingIcon을 클릭하면 custom_dialog layout이 나오도록 이벤트 처리
        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)
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
            Toast.makeText(this,"수정 버튼을 눌렀습니다.",Toast.LENGTH_LONG).show()

            val intent = Intent(this, LostBoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }
        // 삭제버튼을 클릭했을 때
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
            FBRef.lostboardRef.child(key).removeValue()
            Toast.makeText(this,"삭제완료",Toast.LENGTH_LONG).show()

            finish()
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

    // 게시글 데이터를 가져오는 함수
    private fun getBoardData(key: String) {
        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(LostBoardModel::class.java)
                if (dataModel != null) {
                    binding.titleArea.setText(dataModel.title)
                    binding.textArea.setText(dataModel.content)
                    binding.timeArea.setText(dataModel.time)
                    val writerUid = dataModel.uid
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
        FBRef.lostboardRef.child(key).addValueEventListener(postListner)
    }
}