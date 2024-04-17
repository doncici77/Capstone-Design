package com.example.giveback.QnABoard

// 게시글의 데이터 구조를 정의
data class KeywordStatusModel (
    val uid: String ="", // 키워드 설정한 사람의 uid
    val category:String, // category
    val status:Boolean // keyword의 상태(on, off)
)