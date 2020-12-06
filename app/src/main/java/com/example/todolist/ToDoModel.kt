package com.example.todolist

import android.content.Context
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import java.security.AccessControlContext


open class ToDoModel: RealmObject() {
    @PrimaryKey
    var id: Long = 0
//    var createTime:String = ""
    var toDoName: String = ""
    var todoDate:String = ""
    var toDoDetail:String = ""


    private fun initRealm(context: Context) : Realm {
        Realm.init(context)
        return Realm.getDefaultInstance()
    }


    fun findALll(context: Context) : RealmResults<ToDoModel> {
        val realm = initRealm(context)
        return realm.where(ToDoModel::class.java).findAll()
    }




//    fun addRealm(context: Context, addModel: ToDoModel) {
    fun addRealm(context: Context) {
        val realm = initRealm(context)
        realm.executeTransaction {
            var id: Long = realm.where(ToDoModel::class.java).count()
            var quizModel = realm.createObject(ToDoModel::class.java, id)
            /*
            quizModel.toDoName = addModel.toDoName
            quizModel.todoDate = addModel.todoDate
            quizModel.toDoDetail = addModel.toDoDetail
            */

            quizModel.toDoName = "TESTtoDoName${id}"
            quizModel.todoDate = "TESTtodoDate"
            quizModel.toDoDetail = "TESTtoDoDetail"
        }
    }

}