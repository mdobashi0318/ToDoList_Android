package com.example.todolist.other

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todolist.R
import com.example.todolist.screen.MainActivity
import com.example.todolist.screen.TodoDetailActivity

class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (context != null) {
            val title = intent.getStringExtra("title") ?: "期限切れのTodoがあります"
            val createTime = intent.getStringExtra("createTime") ?: ""

            MainActivity.sendNotification(
                context,
                title,
                createTime
            )

        }
    }
}