package com.example.giveback.LostBoard

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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// 리스트뷰와 게시글 데이터를 연결해주는 게시물 리스트 어댑터
class LostBoardListLVAdapter(val boardList : MutableList<LostBoardModel>, val boardKeyList: MutableList<String>): BaseAdapter() {
    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // view를 가져와서 item과 연결해주는 부분
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView

        //if(view== null) {

        view = LayoutInflater.from(parent?.context).inflate(R.layout.lost_board_list_item, parent,false)
        //}

        val imageViewFromFB = view?.findViewById<ImageView>(R.id.imageArea)

        val storageReference = Firebase.storage.reference.child("${boardKeyList[position]}0.png")

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result
                if (imageViewFromFB != null) {
                    Glide.with(view)
                        .load(downloadUrl)
                        .into(imageViewFromFB)
                }
            } else {
                imageViewFromFB?.isVisible = false
            }
        }

        val title = view?.findViewById<TextView>(R.id.titleArea)
        val LostLocation = view?.findViewById<TextView>(R.id.lostlocationArea)
        val LostDate = view?.findViewById<TextView>(R.id.lostDateArea)

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.itemView)
        if(boardList[position].uid.equals(FBAuth.getUid())) {
            itemLinearLayoutView?.setBackgroundColor(Color.parseColor("#EEEEEE"))
        }

        title!!.text = "분실물명: ${boardList[position].title}"
        LostLocation!!.text = "분실장소: ${boardList[position].lostLocation} ${boardList[position].lostdetailLocation}"
        LostDate!!.text = boardList[position].lostDate

        return view!!
    }
}