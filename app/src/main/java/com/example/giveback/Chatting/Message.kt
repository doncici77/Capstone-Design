package com.example.giveback.Chatting

data class Message(
    var message: String?, // 메시지
    var sendId: String? // 보낸 사람의 uid
){
    constructor():this("","")
}