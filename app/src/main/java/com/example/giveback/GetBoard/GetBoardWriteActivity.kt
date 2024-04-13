package com.example.giveback.GetBoard

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.giveback.R
import com.example.giveback.WebviewActivity
import com.example.giveback.databinding.ActivityGetBoardWriteBinding
import com.example.giveback.utils.FBAuth
import com.example.giveback.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.Calendar

// 게시글 작성 페이지
class GetBoardWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGetBoardWriteBinding

    private var isImageUpload = false

    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email.toString()

    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_get_board_write)

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

        val detailget = binding.detailgetArea.text

        // 보관위치와 관련한 드롭다운 메뉴 코드입니다.
        // R.id.gender_spinner 는 1번에서 지정한 Spinner 태그의 ID 입니다.
        val keepSpinner: Spinner = findViewById(R.id.keeplocationArea)

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

        val detailkeep = binding.detailkeepArea.text

        dialog = Dialog(this)

        // 게시글 작성 버튼을 눌렀을 때 파이어베이스에 게시글과 이미지를 넣는다.
        binding.writeBtn.setOnClickListener {

            // uid를 가져온다.
            val uid = FBAuth.getUid()

            val getUid = uid.toString()
            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val getDate = binding.getDateArea.text.toString()
            val getLocation = "${getSpinner.selectedItem.toString()} ${detailget}".toString()
            val keepLocation = "${keepSpinner.selectedItem.toString()} ${detailkeep}".toString()

            // 키부터 생성하고 데이터베이스에 저장하도록 수정
            val key = FBRef.getboardRef.push().key.toString()


            // 습득명은 필수로 입력되어야 한다.
            if(title.isEmpty()){
                Toast.makeText(this,"습득명을 선택해주세요",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 습득날짜는 필수로 입력되어야 한다.
            if(getDate.isEmpty()){
                Toast.makeText(this,"습득날짜를 선택해주세요",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val alertDialog = AlertDialog.Builder(this)
                .setIcon(R.drawable.chat)
                .setTitle("습득물 게시글 등록")
                .setMessage("등록된 게시글은 2024-05-05까지 유지되며 그 이후 자동으로 삭제됩니다.")
                .setPositiveButton("확인") { dialog, which ->
                    // 파이어 베이스에 데이터를 저장한다.
                    FBRef.getboardRef
                        .child(key) // 랜덤한 값
                        .setValue(GetBoardModel(uid, email, title, getDate, getLocation, keepLocation, content,))

                    Toast.makeText(this,"게시글 입력 완료", Toast.LENGTH_LONG).show()

                    // 이미지를 Firebase 스토리지에 업로드
                    if(isImageUpload) {
                        imageUpload(key)
                    }

                    finish()
                }
                .setNegativeButton("취소", null)
                .create()
            alertDialog.show()
        }

        // 이미지 영역을 클릭했을 때 이미지를 업로드한다.
        binding.imageArea1.setOnClickListener {
            showImageUploadDialog()
        }

    }

    // 이미지를 업로드하는 함수
    private fun imageUpload(key: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.png")


        // 이미지 업로드
        val imageView = binding.imageArea1
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()

        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = mountainsRef.putBytes(data)

        uploadTask.addOnFailureListener {
            // 업로드 실패 처리
        }
    }

    private fun showImageUploadDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_image_upload) // dialog_image_upload.xml 파일을 생성해주세요

        // 카메라로 사진 찍기 버튼 클릭 시
        val cameraButton = dialog.findViewById<Button>(R.id.cameraButton)
        cameraButton.setOnClickListener {

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            val cameraPermissionCheck = ContextCompat.checkSelfPermission(
                this@GetBoardWriteActivity,
                android.Manifest.permission.CAMERA
            )
            if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED) { // 권한이 없는 경우
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    1000
                )
            } else { //권한이 있는 경우
                cameraIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(cameraIntent, 200)
                    isImageUpload = true
                    dialog.dismiss()
                }
            }
        }

        // 갤러리에서 사진 선택 버튼 클릭 시
        val galleryButton = dialog.findViewById<Button>(R.id.galleryButton)
        galleryButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
            dialog.dismiss()
        }

        // close이미지 클릭 시
        val closeBtn = dialog.findViewById<ImageView>(R.id.closeBtn)
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        // close버튼 클릭 시
        val closeButton = dialog.findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        // 다이얼로그 띄우기
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.imageArea1.setImageURI(data?.data)
        } else if(resultCode == RESULT_OK && requestCode == 200) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imageArea1.setImageBitmap(imageBitmap)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) { //거부
                Toast.makeText(this@GetBoardWriteActivity, "카메라 권한을 허용하고 이용해주세요 ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}