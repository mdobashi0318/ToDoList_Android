package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var todoModel = ToDoModel().findALll(this)
        var dataArray = mutableListOf<ToDoModel>()

        if(!todoModel.isEmpty()) {
            for (todo in todoModel) {
                Log.d("ToDoModel","todo: ${todo.toDoName}")
                dataArray.add(todo)
            }
//            val adapter = ArrayAdapter<ToDoModel>(
//                applicationContext,
//                android.R.layout.simple_list_item_1,
//                dataArray
//            )
//            todoList.adapter = adapter
        }

        floatingActionButton.setOnClickListener {
            ToDoModel().addRealm(this)
        }
    }




}