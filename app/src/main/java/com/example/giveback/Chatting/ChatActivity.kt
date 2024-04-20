package com.example.giveback.Chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.giveback.R
import com.example.giveback.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    lateinit var mAuth: FirebaseAuth //인증 객체
    lateinit var mDbRef: DatabaseReference//DB 객체

    private lateinit var receiverRoom: String //받는 대화방
    private lateinit var senderRoom: String //보낸 대화방

    private lateinit var receiverUid: String
    private lateinit var receiverEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        receiverEmail =  intent.getStringExtra("email").toString()
        receiverUid =  intent.getStringExtra("uid").toString()

        //액션바에 상대방 이름 보여주기
        binding.topBar.text = "습득자: ${receiverEmail}님과의 채팅방입니다."

        // 파이어베이스 인증, 데이터베이스 초기화
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        //접속자 uId
        val senderUid = mAuth.currentUser?.uid

        //보낸이방
        senderRoom = receiverUid + senderUid

        //받는이방
        receiverRoom = senderUid + receiverUid

        //메시지 전송 버튼 이벤트
        binding.sendBtn.setOnClickListener {

            val message = binding.messageEdit.text.toString()
            // 메시지 객체를 생성한다.
            val messageObject = Message(message, senderUid)

            //데이터 저장
            mDbRef.child("chats").child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    //저장 성공하면
                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                        .setValue(messageObject)
                }
        }

    }
}