package com.example.todolist

import android.content.Context
import android.text.Editable
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.createObject
import java.security.AccessControlContext
import java.text.SimpleDateFormat
import java.util.*


open class ToDoModel : RealmObject() {
    @PrimaryKey
    var createTime: String = ""
    var toDoName: String = ""
    var todoDate: String = ""
    var toDoDetail: String = ""


    private fun initRealm(context: Context): Realm {
        Realm.init(context)
        return Realm.getDefaultInstance()
    }


    fun findALll(context: Context): RealmResults<ToDoModel> {
        val realm = initRealm(context)
        return realm.where(ToDoModel::class.java).findAll()
    }


    fun addTodo(
        context: Context,
        toDoName: String,
        date: String,
        time: String,
        toDoDetail: String,
        success: () -> Unit
    ) {
        val realm = initRealm(context)
        realm.executeTransaction{
            val format = SimpleDateFormat("yyyy/MM/dd HH:mm:SSS")
            var quizModel = realm.createObject<ToDoModel>(format.format(Date()))
            quizModel.toDoName = toDoName
            quizModel.todoDate = "$date  $time"
            quizModel.toDoDetail = toDoDetail
        }
         success()
    }

}