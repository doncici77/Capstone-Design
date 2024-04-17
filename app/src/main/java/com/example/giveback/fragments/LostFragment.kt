package com.example.giveback.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.giveback.LostBoard.LostGetBoardInsideActivity
import com.example.giveback.LostBoard.LostBoardListLVAdapter
import com.example.giveback.LostBoard.LostBoardModel
import com.example.giveback.R
import com.example.giveback.databinding.FragmentLostBinding
import com.example.giveback.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

// 분실물 페이지
class LostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentLostBinding

    private val boardDataList = mutableListOf<LostBoardModel>()
    private val boardKeyList = mutableListOf<String>()

    private lateinit var boardRVAdapter: LostBoardListLVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lost, container, false)

        // BoardListLVAdpater와 연결
        boardRVAdapter = LostBoardListLVAdapter(boardDataList)
        binding.boardListView.adapter = boardRVAdapter

        // 게시글 리스트 중 하나를 클릭했을 때
        binding.boardListView.setOnItemClickListener { parent, view, position, id ->
            /*val intent = Intent(context,GetBoardInsideActivity::class.java)
            intent.putExtra("title",boardDataList[position].title)
            intent.putExtra("content",boardDataList[position].content)
            intent.putExtra("time",boardDataList[position].time)
            startActivity(intent)*/

            val intent = Intent(context, LostGetBoardInsideActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            startActivity(intent)
        }

        getFBBoardData()

        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_LostFragment_to_bookmarkFragment)
        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_LostFragment_to_homeFragment)
        }

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_LostFragment_to_talkFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_LostFragment_to_storeFragment)
        }

        return binding.root
    }

    // 게시글 데이터를 받아오는 함수
    private fun getFBBoardData() {

        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear()

                // dataModel에 있는 데이터를 하나씩 가져오는 부분
                for(dataModel in dataSnapshot.children) {
                    Log.d(ContentValues.TAG, dataModel.toString())

                    val item = dataModel.getValue(LostBoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
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
        FBRef.lostboardRef.addValueEventListener(postListner)
    }
}