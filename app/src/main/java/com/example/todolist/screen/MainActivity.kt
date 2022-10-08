package com.example.todolist.screen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.todolist.*
import com.example.todolist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setSupportActionBar(binding.topAppBar)
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        createChannel(applicationContext)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "com.example.todolist"
        private const val NOTIFICATION_CHANNEL_NAME = "todolist"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "期限がきたら通知を表示します"
        private const val NOTIFICATION_TITLE = "期限切れのTodoがあります"

        fun createChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance).apply {
                    description = NOTIFICATION_CHANNEL_DESCRIPTION
                }
                val manager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)
            }
        }

        fun sendNotification(context: Context, message: String, createTime: String) { if (createTime.isEmpty()) return
            //通知をシステムに登録しています。
            val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                setContentTitle(NOTIFICATION_TITLE)
                setContentText(message)

                val intent = Intent(context, TodoDetailActivity::class.java).apply {
                    putExtra("createTime", createTime)
                    flags =  Intent.FLAG_ACTIVITY_NEW_TASK
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
    }

}



