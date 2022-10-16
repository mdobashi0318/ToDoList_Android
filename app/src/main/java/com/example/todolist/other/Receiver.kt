package com.example.todolist.other

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (context != null) {
            val message = intent.getStringExtra("title") ?: ""
            val createTime = intent.getStringExtra("createTime") ?: ""

            Notification.sendNotification(
                context,
                message,
                createTime
            )

        }
    }
}