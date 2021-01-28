package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var todoModel = ToDoModel().findALll(this)

        if(!todoModel.isEmpty()) {
            val adapter = TodoListAdapter(todoModel) { todo -> onClick(todo) }
            val layoutManager = LinearLayoutManager(this)
            // アダプターとレイアウトマネージャーをセット
            todoRecyclerView.layoutManager = layoutManager
            todoRecyclerView.adapter = adapter
            todoRecyclerView.setHasFixedSize(true)
        }

        floatingActionButton.setOnClickListener {
            ToDoModel().addRealm(this)
        }
    }

    private fun onClick(todo: ToDoModel) {
        Toast.makeText(applicationContext, "{${todo.toDoName}}が押されました",  Toast.LENGTH_SHORT).show()
    }

}



