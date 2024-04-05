package com.example.giveback.LostBoard

// 게시글의 데이터 구조를 정의
data class LostBoardModel (
    val title: String = "",
    val content: String = "", // 상세 내용
    val uid: String  = "",
    val time: String = "",
)