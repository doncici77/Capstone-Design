package com.example.giveback.GetBoard

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.giveback.R
import com.example.giveback.WebviewActivity
import com.example.giveback.databinding.ActivityGetBoardEditBinding
import com.example.giveback.utils.FBAuth
import com.example.giveback.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.Calendar

// 습득물 게시글 수정 페이지
class GetBoardEditActivity : AppCompatActivity() {

    private lateinit var key:String

    private lateinit var binding: ActivityGetBoardEditBinding

    private val TAG = GetBoardEditActivity::class.java.simpleName

    private lateinit var writerUid : String

    private var isImageUpload = false

    private lateinit var getSpinner: Spinner
    private lateinit var detailget : String

    private lateinit var keepSpinner: Spinner
    private lateinit var detailkeep : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_board_edit)

        key = intent.getStringExtra("key").toString()

        getBoardData(key)
        getImageData(key)

        writerUid = FBAuth.getUid()

        // 수정 버튼을 누르면 게시글과 이미지의 수정이 일어난다.
        binding.editBtn.setOnClickListener {
            editBoardData(key)

            if(isImageUpload) {
                imageUpload(key)
            }
        }

        // 이미지 영역을 클릭했을 때 이미지 업로드를 실행한다.
        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }

        // 습득 위치 옆의 물음표 버튼을 눌렀을 때 학교 건물 웹뷰로 이동한다.
        binding.question.setOnClickListener {
            val intent = Intent(this, WebviewActivity::class.java)
            startActivity(intent)
        }

        // 날짜를 입력받을 때 달력이 나오고 달력에서 날짜를 선택하면 선택한 날짜가 text로 들어간다.
        val datePickerListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            // 선택한 날짜를 원하는 형식으로 텍스트로 변환
            val selectedDateText = "${year}년 ${month + 1}월 ${dayOfMonth}일"

            // 버튼의 텍스트를 선택한 날짜로 변경
            binding.getDateArea.text = selectedDateText
        }

        // 버튼 클릭 시 DatePickerDialog를 띄우는 코드
        binding.getDateArea.setOnClickListener {
            val calendar = Calendar.getInstance()
            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

            // DatePickerDialog 생성
            val datePickerDialog = DatePickerDialog(this, datePickerListener, initialYear, initialMonth, initialDay)
            datePickerDialog.show()
        }

        // 습득위치와 관련한 드롭다운 메뉴 코드입니다.
        // R.id.gender_spinner 는 1번에서 지정한 Spinner 태그의 ID 입니다.
        getSpinner = findViewById(R.id.getlocationArea)

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

        detailget = binding.detailgetArea.text.toString()

        // 보관위치와 관련한 드롭다운 메뉴 코드입니다.
        // R.id.gender_spinner 는 1번에서 지정한 Spinner 태그의 ID 입니다.
        keepSpinner = findViewById(R.id.keeplocationArea)

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
            keepSpinner.adapter = adapter
        }

        detailkeep = binding.detailkeepArea.text.toString()
    }

    // 게시글을 수정하는 함수
    private fun editBoardData(key: String) {
        FBRef.getboardRef
            .child(key) // 랜덤한 값
            .setValue(
                GetBoardModel(
                    writerUid.toString(),
                    binding.emailArea.text.toString(),
                    binding.titleArea.text.toString(),
                    binding.getDateArea.text.toString(),
                    "${getSpinner.selectedItem.toString()} ${detailget}",
                    "${keepSpinner.selectedItem.toString()} ${detailkeep}",
                    binding.contentArea.text.toString(),
                ))
        Toast.makeText(this,"수정완료", Toast.LENGTH_LONG).show()
        finish()
    }

    // 이미지를 업로드하는 함수
    private fun imageUpload(key: String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key + ".png")

        val imageView = binding.imageArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()

        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.imageArea.setImageURI(data?.data)

        }
    }
    
    // 게시글을 가져오는 함수
    private fun getBoardData(key: String) {
        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(GetBoardModel::class.java)

                if (dataModel != null) {
                    binding.emailArea.setText(dataModel.email)
                    binding.titleArea.setText(dataModel.title)
                    binding.getDateArea.setText(dataModel.getDate)
                    getSpinner.setSelection(0) // 수정할 때 Spinner 인덱스를 0으로 초기화
                    keepSpinner.setSelection(0) // 수정할 때 Spinner 인덱스를 0으로 초기화
                    binding.contentArea.setText(dataModel.content)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.getboardRef.child(key).addValueEventListener(postListner)
    }

    // 이미지데이터를 불러오는 함수
    private fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child(key + ".png")

        val imageViewFromFB = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task->
            if(task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            }else {

            }
        })
    }
}