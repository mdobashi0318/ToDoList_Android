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