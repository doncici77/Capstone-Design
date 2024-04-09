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

        // 습득 위치 옆의 물음표 버튼을 눌렀을 때 학교 건물 웹뷰로 이동한다.
        binding.question.setOnClickListener {
            val intent = Intent(this, WebviewActivity::class.java)
            startActivity(intent)
        }

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

        // 습득위치와 관련한 드롭다운 메뉴 코드입니다.
        // R.id.gender_spinner 는 1번에서 지정한 Spinner 태그의 ID 입니다.
        val getSpinner: Spinner = findViewById(R.id.getlocationArea)

        ArrayAdapter.createFromResource(
            this,

            // 설정한 string-array 태그의 name 입니다.
            R.array.getlocation_array,

            // android.R.layout.simple_spinner_dropdown_item 은 android 에서 기본 제공
            // 되는 layout 입니다. 이 부분은 "선택된 item" 부분의 layout을 결정합니다.
            R.layout.getlocation_spinner_item

        ).also { adapter ->

            // android.R.layout.simple_spinner_dropdown_item 도 android 에서 기본 제공
            // 되는 layout 입니다. 이 부분은 "선택할 item 목록" 부분의 layout을 결정합니다.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            getSpinner.adapter = adapter
        }

        // 검색 버튼을 눌렀을 때 검색된 화면(SearchedActivity로 이동
        binding.searchBtn.setOnClickListener{

            // 시작일은 필수로 입력되어야 한다.
            if(binding.getStartDate.text.toString() == ""){
                Toast.makeText(this,"시작일을 선택해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 종료일은 필수로 입력되어야 한다.
            if(binding.getEndDate.text.toString() == ""){
                Toast.makeText(this,"종료일을 선택해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val intent = Intent(this, SearchedActivity::class.java)
            intent.putExtra("물품명",binding.titleArea.text.toString())
            intent.putExtra("시작일", binding.getStartDate.text.toString())
            intent.putExtra("종료일", binding.getEndDate.text.toString())
            startActivity(intent)
        }
    }
}