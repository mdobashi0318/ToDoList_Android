package com.example.todolist.other

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todolist.R
import com.example.todolist.screen.TodoApplication
import com.example.todolist.screen.TodoDetailActivity
import java.util.*

class Notification {

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "com.example.todolist"
        private const val NOTIFICATION_CHANNEL_NAME = "todolist"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "期限がきたら通知を表示します"
        private const val NOTIFICATION_TITLE = "期限切れのTodoがあります"

        fun createChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    importance
                ).apply {
                    description = NOTIFICATION_CHANNEL_DESCRIPTION
                }
                val manager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)
            }
        }

        fun sendNotification(context: Context, message: String, createTime: String) {
            if (createTime.isEmpty()) return
            val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                setContentTitle(NOTIFICATION_TITLE)
                setContentText(message)

                val intent = Intent(context, TodoDetailActivity::class.java).apply {
                    putExtra("createTime", createTime)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }

                val pending: PendingIntent = PendingIntent.getActivity(
                    context,
                    createTime.toInt(),
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                setContentIntent(pending)
                setAutoCancel(true)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }

            NotificationManagerCompat.from(context).notify(createTime.toInt(), builder.build())
        }


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