package com.example.todolist.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.todolist.other.Mode
import com.example.todolist.R
import com.example.todolist.model.ToDoModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_todo_detail.*

class TodoDetailActivity : AppCompatActivity() {

    private lateinit var todo: ToDoModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)


        topAppBar.setNavigationOnClickListener {
            finish()
        }

        topAppBar.setOnMenuItemClickListener { item ->
            // Handle item selection
            when (item.itemId) {
                R.id.detail_edit -> {
                    // Todo編集画面に遷移する
                    val intent = Intent(this, TodoRegistrationActivity::class.java)
                    intent.putExtra("mode", Mode.Edit.name)
                    intent.putExtra("createTime", todo.createTime)
                    startActivity(intent)
                    true
                }
                R.id.detail_delete -> {
                    // Todoを削除する
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Todoを削除しますか?")
                        .setPositiveButton(R.string.deleteButton) { _, _ ->
                            ToDoModel().delete(applicationContext, todo.createTime) {
                                MaterialAlertDialogBuilder(this)
                                    .setTitle("削除しました")
                                    .setPositiveButton(R.string.closeButton) { _, _ ->
                                        finish()
                                    }
                                    .show()
                            }
                        }
                        .setNegativeButton(R.string.cancelButton, null)
                        .show()
                    true
                }
                android.R.id.home -> {
                    /// 前の画面に戻る
                    finish()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

    }


    override fun onResume() {
        super.onResume()
        intent.getStringExtra("todo")?.let { date ->
            ToDoModel().find(applicationContext, date)?.let {
                todo = it
            }
        }


        titleTextView.text = todo.toDoName
        dateTextView.text = "${todo.todoDate} ${todo.todoTime}"
        detailTextView.text = todo.toDoDetail
    }


}