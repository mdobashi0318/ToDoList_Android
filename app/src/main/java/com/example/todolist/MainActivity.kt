package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, TodoRegistrationActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()

        var todoModel = ToDoModel().findAllTodo(this)

        if (!todoModel.isEmpty()) {
            val adapter = TodoListAdapter(todoModel) { todo -> onClick(todo) }
            val layoutManager = LinearLayoutManager(this)
            // アダプターとレイアウトマネージャーをセット
            todoRecyclerView.layoutManager = layoutManager
            todoRecyclerView.adapter = adapter
            todoRecyclerView.setHasFixedSize(true)
        }

    }


    private fun onClick(todo: ToDoModel) {
        val intent = Intent(this, TodoDetailActivity::class.java)
        startActivity(intent)
    }

}



