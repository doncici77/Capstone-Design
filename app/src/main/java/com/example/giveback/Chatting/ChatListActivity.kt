package com.example.giveback.Chatting

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.giveback.GetBoard.GetBoardListLVAdapter
import com.example.giveback.GetBoard.GetBoardModel
import com.example.giveback.R
import com.example.giveback.databinding.ActivityChatListBinding
import com.example.giveback.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatListBinding

    private val chatDataList = mutableListOf<Message>()
    private val chatKeyList = mutableListOf<String>()

    private lateinit var ChatRVAdapter: ChatListLVAdapter

    lateinit var mAuth: FirebaseAuth //인증 객체
    lateinit var mDbRef: DatabaseReference//DB 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_list)

        // BoardListLVAdpater와 연결
        ChatRVAdapter = ChatListLVAdapter(chatDataList, chatKeyList)
        binding.chatListView.adapter = ChatRVAdapter

        // 파이어베이스 인증, 데이터베이스 초기화
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

    }
}