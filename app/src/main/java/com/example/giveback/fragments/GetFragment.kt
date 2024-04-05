package com.example.giveback.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.giveback.R
import com.example.giveback.databinding.FragmentGetBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.example.giveback.GetBoard.GetGetBoardInsideActivity
import com.example.giveback.GetBoard.GetBoardListLVAdapter
import com.example.giveback.GetBoard.GetBoardModel
import com.example.giveback.utils.FBRef

// 습득물 페이지
class GetFragment : Fragment() {

    private lateinit var binding: FragmentGetBinding

    private val boardDataList = mutableListOf<GetBoardModel>()
    private val boardKeyList = mutableListOf<String>()

    private lateinit var boardRVAdapter: GetBoardListLVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get, container, false)

        // BoardListLVAdpater와 연결
        boardRVAdapter = GetBoardListLVAdapter(boardDataList)
        binding.boardListView.adapter = boardRVAdapter

        // 게시글 리스트 중 하나를 클릭했을 때
        binding.boardListView.setOnItemClickListener { parent, view, position, id ->
            /*val intent = Intent(context,GetBoardInsideActivity::class.java)
            intent.putExtra("title",boardDataList[position].title)
            intent.putExtra("content",boardDataList[position].content)
            intent.putExtra("time",boardDataList[position].time)
            startActivity(intent)*/

            val intent = Intent(context, GetGetBoardInsideActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            startActivity(intent)
        }

        getFBBoardData()

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_getFragment_to_tipFragment)
        }

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_getFragment_to_talkFragment)
        }

        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_getFragment_to_bookmarkFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_getFragment_to_storeFragment)
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
                    Log.d(TAG, dataModel.toString())

                    val item = dataModel.getValue(GetBoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
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
