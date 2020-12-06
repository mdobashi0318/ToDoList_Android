package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_todo_recycler_view_item.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var todoModel = ToDoModel().findALll(this)

        if(!todoModel.isEmpty()) {
            val adapter = TodoListAdapter(todoModel)
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




}



class TodoListAdapter(private val todoModel: MutableList<ToDoModel>) :
    RecyclerView.Adapter<TodoListAdapter.TodoListAdapterHolder>() {

    class TodoListAdapterHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val title = view.title!!
        val todoDate = view.todoDate!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListAdapterHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.activity_todo_recycler_view_item, parent, false)
        return TodoListAdapterHolder(item)
    }

    override fun onBindViewHolder(holder: TodoListAdapterHolder, position: Int) {
        holder.title.text = todoModel[position].toDoName
        holder.todoDate.text = todoModel[position].todoDate
    }

    override fun getItemCount(): Int {
        return todoModel.size
    }

}
