package com.example.todolist.extensions


/**
* 文字の長さは1文字だった場合は1文字目に0を追加する
**/
fun String.addFirstZero() = if (this.length == 1) "0$this" else "$this"