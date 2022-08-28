package com.example.todolist.other

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.todolist.screen.TodoApplication
import java.util.*

class Notification {

    companion object {
        /**
         * ローカル通知を設定する
         * @param context
         * @param date 月日
         * @param time 時間
         * @param createTime 通知設定の識別値
         * @param title 通知タイトル
         */
        fun setNotification(
            context: Context,
            date: String,
            time: String,
            createTime: String,
            title: String,
        ) {

            val dateList = date.split("/")
            val timeList = time.split(":")

            val month = dateList[1].toInt()
            val day = dateList[2].toInt()
            val hour = timeList[0].toInt()
            val min = timeList[1].toInt()

            val calender = Calendar.getInstance()

            val intent = Intent(context, Receiver::class.java).apply {
                putExtra("title", title)
                putExtra("createTime", createTime)
            }

            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    createTime.toInt(),
                    intent,
                    PendingIntent.FLAG_MUTABLE
                )
            calender.set(Calendar.MONTH, month - 1)
            calender.set(Calendar.DAY_OF_MONTH, day)
            calender.set(Calendar.HOUR_OF_DAY, hour)
            calender.set(Calendar.MINUTE, min)
            calender.set(Calendar.SECOND, 0)

            val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC, calender.timeInMillis, pendingIntent)

        }


        /**
         * ModelからTodoを全件取得してから、ローカル通知を全件キャンセルする
         * @param context
         */
        suspend fun cancelAllNotification(context: Context, success: () -> Unit) {
            val intent = Intent(context, Receiver::class.java)
            val model = TodoApplication.database.todoDao().getAll()
            model.forEach {
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    it.createTime.toInt(),
                    intent,
                    PendingIntent.FLAG_MUTABLE
                )
                val alarmManager =
                    context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
            }
            success()
        }


        /**
         * ローカル通知をキャンセルする
         * @param context
         * @param createTime キャンセル通知設定の識別値
         */
        fun cancelNotification(context: Context, createTime: String) {
            val intent = Intent(context, Receiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    createTime.toInt(),
                    intent,
                    PendingIntent.FLAG_MUTABLE
                )

            val alarmManager =
                context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }
}