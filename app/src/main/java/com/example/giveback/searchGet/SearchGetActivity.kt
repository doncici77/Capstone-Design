package com.example.giveback.searchGet

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.giveback.R
import com.example.giveback.WebviewActivity
import com.example.giveback.databinding.ActivityGetBoardWriteBinding
import com.example.giveback.databinding.ActivitySearchGetBinding
import com.example.giveback.fragments.GetFragment
import java.util.Calendar

class SearchGetActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchGetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_get)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_search_get)

        // 날짜를 입력받을 때 달력이 나오고 달력에서 날짜를 선택하면 선택한 날짜가 text로 들어간다.
        val datePickerListener1 = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            // 선택한 날짜를 원하는 형식으로 텍스트로 변환
            val selectedDateText = "${year}년 ${month + 1}월 ${dayOfMonth}일"

            // 버튼의 텍스트를 선택한 날짜로 변경
            binding.getStartDate.text = selectedDateText
        }

        val datePickerListener2 = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            // 선택한 날짜를 원하는 형식으로 텍스트로 변환
            val selectedDateText = "${year}년 ${month + 1}월 ${dayOfMonth}일"

            // 버튼의 텍스트를 선택한 날짜로 변경
            binding.getEndDate.text = selectedDateText
        }

        // 버튼 클릭 시 DatePickerDialog를 띄우는 코드
        binding.getStartDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

            // DatePickerDialog 생성
            val datePickerDialog = DatePickerDialog(this, datePickerListener1, initialYear, initialMonth, initialDay)
            datePickerDialog.show()
        }

        // 버튼 클릭 시 DatePickerDialog를 띄우는 코드
        binding.getEndDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

            // DatePickerDialog 생성
            val datePickerDialog = DatePickerDialog(this, datePickerListener2, initialYear, initialMonth, initialDay)
            datePickerDialog.show()
        }

        // 검색 버튼을 눌렀을 때 검색된 화면(SearchedActivity로 이동
        binding.searchBtn.setOnClickListener{


            val intent = Intent(this, SearchedActivity::class.java)
            intent.putExtra("물품명",binding.titleArea.text.toString())
            intent.putExtra("시작일", binding.getStartDate.text.toString())
            intent.putExtra("종료일", binding.getEndDate.text.toString())
            startActivity(intent)
        }
    }
}