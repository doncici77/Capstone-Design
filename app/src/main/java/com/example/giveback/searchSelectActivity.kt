package com.example.giveback

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.giveback.GetBoard.GetBoardEditActivity
import com.example.giveback.databinding.ActivitySearchSelectBinding
import com.example.giveback.searchGet.SearchGetActivity

class searchSelectActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_search_select)

        binding.GetSearchBtn.setOnClickListener {
            val intent = Intent(this, SearchGetActivity::class.java)
            startActivity(intent)
        }

        binding.LostSearchBtn.setOnClickListener {

        }
    }
}