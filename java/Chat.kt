package com.example.assignment2

data class Chat(
    var sender: String,
    var receiver: String,
    var message: String,
    var audioUrl: String? = null,
    var imageUrl: String? = null,
    var key: String = ""
) {
    constructor() : this("", "", "")
}
