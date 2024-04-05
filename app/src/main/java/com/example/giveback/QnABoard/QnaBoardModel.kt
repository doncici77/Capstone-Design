package com.example.giveback.QnABoard

// 게시글의 데이터 구조를 정의
data class QnaBoardModel (
    val title: String = "",
    val content: String = "",
    val uid: String  = "",
    val time: String = ""
)