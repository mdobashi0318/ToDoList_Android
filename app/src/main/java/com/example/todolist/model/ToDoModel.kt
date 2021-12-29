package com.example.todolist.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.todolist.other.Receiver
import com.example.todolist.screen.MainActivity
import io.realm.R
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.createObject
import java.text.SimpleDateFormat
import java.util.*


open class ToDoModel : RealmObject() {
    @PrimaryKey
    var createTime: String = ""
    var toDoName: String = ""
    var todoDate: String = ""
    var todoTime: String = ""
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
            todo.todoDate = date
            todo.todoTime = time
            todo.toDoDetail = toDoDetail
        }


        val dateList = "2021/12/29".split("/")
        val timeList = "21:42".split(":")
        setNotification(context, dateList[1].toInt(), dateList[2].toInt(), timeList[0].toInt(), timeList[1].toInt())
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
            todo.todoDate = date
            todo.todoTime = time
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


    private fun setNotification(context: Context, month: Int, day: Int, hour: Int, min: Int) {
        val calender = Calendar.getInstance()
        val intent = Intent(context, Receiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        calender.set(Calendar.MONTH, month - 1)
        calender.set(Calendar.DAY_OF_MONTH, day)
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, min)
        calender.set(Calendar.SECOND, 0)

        val alarmManager = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC, calender.timeInMillis, pendingIntent)
    }


}