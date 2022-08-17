package com.example.todolist.screen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavArgs
import androidx.navigation.findNavController
import androidx.navigation.navArgument
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.*
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.model.ToDoModel
import com.example.todolist.uiparts.TodoListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setSupportActionBar(binding.topAppBar)
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
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

        fun sendNotification(context: Context, message: String, createTime: String) { if (createTime.isEmpty()) return

            val channelId = NOTIFICATION_CHANNEL_ID
            val channelName = NOTIFICATION_CHANNEL_NAME
            val channelDescription = NOTIFICATION_CHANNEL_DESCRIPTION

            //Android 8.0 以上ではアプリの通知チャンネルを登録することが必要。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, channelName, importance).apply {
                    description = channelDescription
                }
                val manager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)
            }


            //通知をシステムに登録しています。
            val builder = NotificationCompat.Builder(context, channelId).apply {
                setSmallIcon(R.drawable.ic_launcher_foreground)
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



