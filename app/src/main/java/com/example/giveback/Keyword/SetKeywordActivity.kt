package com.example.giveback.Keyword

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.giveback.GetBoard.GetBoardModel
import com.example.giveback.R
import com.example.giveback.databinding.ActivitySetKeywordBinding
import com.example.giveback.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SetKeywordActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetKeywordBinding

    private val PERMISSION_REQUEST_CODE = 5000

    var auth = FirebaseAuth.getInstance()

    var myUid = auth.currentUser?.uid.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_keyword)

        createNotificationChannel()

        getKeyword()

        binding.settingBtn.setOnClickListener {

            val alertDialog = AlertDialog.Builder(this)
                .setIcon(R.drawable.chat)
                .setTitle("${binding.keywordArea1.text.toString()}을 키워드로 등록합니다.")
                .setMessage("등록된 키워드로 습득물 게시글이 올라올 시 알림이 발생합니다.")
                .setPositiveButton("확인") { dialog, which ->
                    getKeyword()
                    finish()
                }
                .setNegativeButton("취소", null)
                .create()
            alertDialog.show()
        }
    }

    private fun getKeyword() {
        // ChildEventListener 등록
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // 글이 추가되었을 때 처리하는 로직
                val post = snapshot.getValue(GetBoardModel::class.java)

                if(post?.uid.toString() == myUid && post?.title == binding.keywordArea1.text.toString()){
                    // notification
                    sendNotification()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // 글이 변경되었을 때 처리하는 로직
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // 글이 삭제되었을 때 처리하는 로직
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // 글의 순서가 변경되었을 때 처리하는 로직
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러 처리
            }
        }

        // posts 경로에 ChildEventListener 등록
        FBRef.getboardRef.addChildEventListener(childEventListener)
    }

    private fun permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(applicationContext, "Permission is denied", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "Permission is granted", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("TestChannel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification() {

        val intent = Intent(this, KeywordSearchedActivity::class.java) // YourTargetActivity는 이동할 화면의 클래스입니다.
        intent.putExtra("물품명",binding.keywordArea1.text.toString())
        intent.putExtra("시작일", "2024년 1월 1일")
        intent.putExtra("종료일", "2030년 1월 1일")
        intent.putExtra("습득위치", "123")
        val pendingIntent = PendingIntent.getActivity(this@SetKeywordActivity, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE)

        val builder = NotificationCompat.Builder(this, "TestChannel")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("키워드로 등록한 ${binding.keywordArea1.text.toString()} 게시글이 올라왔습니다.")
            .setContentText("앱을 실행하여 분실물을 확인하세요")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        // 알림 표시
        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }
}