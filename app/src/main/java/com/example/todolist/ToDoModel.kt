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


    fun findAll(context: Context): RealmResults<ToDoModel> {
        val realm = initRealm(context)
        return realm.where(ToDoModel::class.java).findAll()
    }


    fun find(context: Context, createTime: String): ToDoModel? {
        val realm = initRealm(context)
        return realm.where(ToDoModel::class.java).equalTo("createTime", createTime).findFirst()
    }

    fun add(
        context: Context,
        toDoName: String,
        date: String,
        time: String,
        toDoDetail: String,
        success: () -> Unit
    ) {
        val realm = initRealm(context)
        realm.executeTransaction {
            val format = SimpleDateFormat("yyyy/MM/dd HH:mm:SSS")
            var todo = realm.createObject<ToDoModel>(format.format(Date()))
            todo.toDoName = toDoName
            todo.todoDate = "$date  $time"
            todo.toDoDetail = toDoDetail
        }
        success()
    }

    fun update(
        context: Context,
        toDoName: String,
        date: String,
        time: String,
        toDoDetail: String,
        createTime: String,
        success: () -> Unit
    ) {
        val realm = initRealm(context)
        var todo = find(context, createTime)
        if(todo == null) return


        realm.executeTransaction {
            todo.toDoName = toDoName
            todo.todoDate = "$date  $time"
            todo.toDoDetail = toDoDetail
        }
        success()
    }


    fun delete(context: Context, createTime: String?, success: () -> Unit) {
        if (createTime == null) {
            return
        }

        val realm = initRealm(context)
        realm.executeTransaction {
            find(context, createTime)?.deleteFromRealm()
        }
        success()
    }


}