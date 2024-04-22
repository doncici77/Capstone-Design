package com.example.giveback.Chatting

import com.example.giveback.GetBoard.GetBoardModel
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.giveback.R
import com.example.giveback.utils.FBAuth
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// 리스트뷰와 게시글 데이터를 연결해주는 게시물 리스트 어댑터
class ChatListLVAdapter(val chatList : MutableList<Message>, val chatKeyList: MutableList<String>): BaseAdapter() {
    override fun getCount(): Int {
        return chatList.size
    }

    override fun getItem(position: Int): Any {
        return chatList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // view를 가져와서 item과 연결해주는 부분
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView

        //if(view== null) {

        view = LayoutInflater.from(parent?.context).inflate(R.layout.item_chat, parent,false)
        //}

        val sendUid = view?.findViewById<TextView>(R.id.senduid)


        sendUid!!.text = "습득명: ${chatList[position].sendId}"

        return view!!
    }


}