package com.example.todolist.screen

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.*
import com.example.todolist.model.ToDoModel
import com.example.todolist.other.Mode
import com.example.todolist.uiparts.TodoListAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton.setOnClickListener {
            // Todo作成画面に遷移する
            val intent = Intent(this, TodoRegistrationActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()

        var todoModel = ToDoModel().findAll(this)
        val adapter = TodoListAdapter(todoModel) { todo -> onClick(todo) }
        val layoutManager = LinearLayoutManager(this)
        // アダプターとレイアウトマネージャーをセット
        todoRecyclerView.layoutManager = layoutManager
        todoRecyclerView.adapter = adapter
        todoRecyclerView.setHasFixedSize(true)

    }


    /**
     * 選択したTodoの詳細に遷移する
     */
    private fun onClick(todo: ToDoModel) {
        val intent = Intent(this, TodoDetailActivity::class.java)
        intent.putExtra("todo", todo.createTime)
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.trash -> {
                // Todoを全件削除する
                AlertDialog.Builder(this)
                    .setTitle("全件削除しますか？")
                    .setPositiveButton("削除") { _, _ ->
                        ToDoModel().allDelete(applicationContext) {
                            AlertDialog.Builder(this)
                                .setTitle("削除しました")
                                .setPositiveButton("閉じる") { _ , _ -> onResume() }
                                .show()
                        }
                    }
                    .setNegativeButton("キャンセル", null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "com.example.todolist"
        private const val NOTIFICATION_CHANNEL_NAME = "todolist"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "期限がきたら通知を表示します"
        private const val NOTIFICATION_TITLE = "期限切れのTodoがあります"
        private const val NOTIFICATION_MESSAGE = ""
        fun sendNotification(context: Context) {
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
                setContentText(NOTIFICATION_MESSAGE)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }

            NotificationManagerCompat.from(context).notify(0, builder.build())
        }
    }

}



