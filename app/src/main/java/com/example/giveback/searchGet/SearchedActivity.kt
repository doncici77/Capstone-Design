package com.example.giveback.searchGet

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.giveback.GetBoard.GetBoardInsideActivity
import com.example.giveback.GetBoard.GetBoardListLVAdapter
import com.example.giveback.GetBoard.GetBoardModel
import com.example.giveback.LostBoard.LostGetBoardInsideActivity
import com.example.giveback.MainActivity
import com.example.giveback.R
import com.example.giveback.databinding.ActivitySearchedBinding
import com.example.giveback.fragments.GetFragment
import com.example.giveback.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date


class SearchedActivity : AppCompatActivity() {

    private val TAG = SearchedActivity::class.java.simpleName

    private val boardDataList = mutableListOf<GetBoardModel>()
    private val boardKeyList = mutableListOf<String>()

    private lateinit var boardRVAdapter: GetBoardListLVAdapter

    private lateinit var binding: ActivitySearchedBinding

    private lateinit var searchtitle: String
    private lateinit var getlocation: String

    val sdf = SimpleDateFormat("yyyy년 MM월 dd일")

    private lateinit var startDate: Date
    private lateinit var endDate: Date
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_searched)

        // BoardListLVAdpater와 연결
        boardRVAdapter = GetBoardListLVAdapter(boardDataList, boardKeyList)
        binding.boardListView.adapter = boardRVAdapter

        // 게시글 리스트 중 하나를 클릭했을 때
        binding.boardListView.setOnItemClickListener { parent, view, position, id ->

            val intent = Intent(this, GetBoardInsideActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            startActivity(intent)
        }

        searchtitle = intent.getStringExtra("물품명").toString()

        if(intent.getStringExtra("시작일").toString() == "" && intent.getStringExtra("종료일").toString() == "") {
            startDate = sdf.parse("2024년 1월 1일")
            endDate = sdf.parse("2030년 1월 1일")
        } else {
            startDate = sdf.parse(intent.getStringExtra("시작일").toString())
            endDate = sdf.parse(intent.getStringExtra("종료일").toString())
        }

        getlocation = intent.getStringExtra("습득위치").toString()

        getFBBoardData(searchtitle)

        // 되돌아기 버튼을 눌렀을 때 GetFragment로 이동
        binding.backGetFragment.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getFBBoardData(searchtitle:String) {

        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear()

                // dataModel에 있는 데이터를 하나씩 가져오는 부분
                for(dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(GetBoardModel::class.java)

                    val sdfDate = sdf.parse(item?.getDate)

                    if(searchtitle.contains(item?.title.toString()) &&
                        (sdfDate <= endDate && sdfDate >= startDate))
                    {
                        boardDataList.add(item!!)
                        boardKeyList.add(dataModel.key.toString())
                    }

                }


                boardKeyList.reverse()
                // 최신 게시글이 앞으로 오도록 리스트를 뒤집는다.
                boardDataList.reverse()

                // boardRVAdapter 동기화
                boardRVAdapter.notifyDataSetChanged()

                Log.d(TAG, boardDataList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.getboardRef.addValueEventListener(postListner)
    }
}