package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_todo_detail.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class TodoDetailActivity : AppCompatActivity() {

    private lateinit var todo: ToDoModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onResume() {
        super.onResume()
        val date = intent.getStringExtra("todo")
        ToDoModel().find(applicationContext, date)?.let {
            todo = it
        }

        titleTextView.text = todo.toDoName
        dateTextView.text = todo.todoDate
        detailTextView.text = todo.toDoDetail
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.detail_edit -> {
                val intent = Intent(this, TodoRegistrationActivity::class.java)
                intent.putExtra("mode", Mode.Edit.name)
                intent.putExtra("createTime", todo.createTime)
                startActivity(intent)
                true
            }
            R.id.detail_delete -> {
                alert("Todoを削除しますか？") {
                    yesButton {
                        ToDoModel().delete(applicationContext, todo.createTime) {
                            alert("削除しました") { yesButton { finish() } }.show()
                        }
                    }
                    noButton { }
                }.show()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}