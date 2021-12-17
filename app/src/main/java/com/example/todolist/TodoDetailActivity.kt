package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_todo_detail.*

class TodoDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val date = intent.getStringExtra("todo")
        var todo = ToDoModel().find(applicationContext, date)


        println("todo; $todo")
        if(todo != null) {
            titleTextView.text = todo.toDoName
            dateTextView.text = todo.todoDate
            detailTextView.text = todo.toDoDetail
        }



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
                startActivity(intent)
                true
            }
            R.id.detail_edit -> {
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