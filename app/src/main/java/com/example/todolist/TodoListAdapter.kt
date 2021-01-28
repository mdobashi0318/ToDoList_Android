package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_todo_recycler_view_item.view.*

class TodoListAdapter(private val todoModel: MutableList<ToDoModel>, private val onClick:(ToDoModel) -> Unit) :
    RecyclerView.Adapter<TodoListAdapter.TodoListAdapterHolder>() {

    class TodoListAdapterHolder(private val view: View, val onClick:(ToDoModel) -> Unit): RecyclerView.ViewHolder(view) {
        private var currentTodo: ToDoModel? = null

        init {
            view.setOnClickListener {
                currentTodo?.let {
                    onClick(it)
                }
            }
        }

        fun bind(todo: ToDoModel) {
            view.title.text = todo.toDoName
            view.todoDate.text = todo.todoDate
            currentTodo = todo
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListAdapterHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.activity_todo_recycler_view_item, parent, false)
        return TodoListAdapterHolder(item, onClick)
    }

    override fun onBindViewHolder(holder: TodoListAdapterHolder, position: Int) {
        val todo = todoModel[position]
        holder.bind(todo)
    }

    override fun getItemCount(): Int {
        return todoModel.size
    }

}
