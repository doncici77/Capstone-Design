package com.example.giveback.Keyword

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.giveback.GetBoard.GetBoardListLVAdapter
import com.example.giveback.GetBoard.GetBoardModel
import com.example.giveback.GetBoard.GetBoardWriteActivity
import com.example.giveback.QnABoard.KeywordStatusModel
import com.example.giveback.R
import com.example.giveback.databinding.ActivityKeywordBinding
import com.example.giveback.databinding.FragmentGetBinding
import com.example.giveback.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class KeywordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKeywordBinding


    private val boardDataList = mutableListOf<KeywordStatusModel>()
    private val boardKeyList = mutableListOf<String>()

    val user = FirebaseAuth.getInstance().currentUser
    val MyUid = user?.uid.toString()

    private lateinit var boardRVAdapter: KeywordListLVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_keyword)
        // KeywordListLVAdpater와 연결
        boardRVAdapter = KeywordListLVAdapter(boardDataList, boardKeyList)
        binding.keywordListView.adapter = boardRVAdapter

        getFBBoardData()

        binding.keywordWriteBtn.setOnClickListener {
            val intent = Intent(this, KeywordWriteActivity::class.java)
            startActivity(intent)
        }

    }

    // 게시글 데이터를 받아오는 함수
    private fun getFBBoardData() {

        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear()

                // dataModel에 있는 데이터를 하나씩 가져오는 부분
                for(dataModel in dataSnapshot.children) {
                    Log.d(ContentValues.TAG, dataModel.toString())

                    val item = dataModel.getValue(KeywordStatusModel::class.java)

                    if(MyUid == item?.uid) {
                        boardDataList.add(item!!)
                        boardKeyList.add(dataModel.key.toString())
                    }
                }

                boardKeyList.reverse()
                // 최신 게시글이 앞으로 오도록 리스트를 뒤집는다.
                boardDataList.reverse()

                // boardRVAdapter 동기화
                boardRVAdapter.notifyDataSetChanged()

                Log.d(ContentValues.TAG, boardDataList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.keywordRef.addValueEventListener(postListner)
    }
}