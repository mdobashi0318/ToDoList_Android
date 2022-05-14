package com.example.todolist.uiparts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.extensions.addFirstZero
import com.example.todolist.model.ToDoModel
import kotlinx.android.synthetic.main.activity_todo_recycler_view_item.view.*

class TodoListAdapter(
    private val todoModel: MutableList<ToDoModel>,
    private val onClick: (ToDoModel) -> Unit
) :
    RecyclerView.Adapter<TodoListAdapter.TodoListAdapterHolder>() {

    class TodoListAdapterHolder(private val view: View, val onClick: (ToDoModel) -> Unit) :
        RecyclerView.ViewHolder(view) {
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

            var hour: String
            var min: String
            val tmpTime = todo.todoTime.split(":")
            min = tmpTime[1].addFirstZero()

            view.todoDate.text = "${todo.todoDate} ${tmpTime[0]}:$min"
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
