package com.example.todolist.uiparts

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.extensions.LocalDateTimeExtension
import com.example.todolist.model.ToDoModel
import com.example.todolist.other.CompletionFlag
import kotlinx.android.synthetic.main.activity_todo_recycler_view_item.view.*
import java.time.temporal.ChronoUnit

class TodoListAdapter(
    private val todoModel: List<ToDoModel>,
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

            view.complete.text =
                if (todo.completionFlag == CompletionFlag.Unfinished.value && ChronoUnit.MINUTES.between(
                        LocalDateTimeExtension.fromStringToDate("${todo.todoDate} ${todo.todoTime}"),
                        LocalDateTimeExtension.now()
                    ) <= 0
                ) {
                    "未完了"
                } else if (todo.completionFlag == CompletionFlag.Unfinished.value && ChronoUnit.MINUTES.between(
                        LocalDateTimeExtension.fromStringToDate("${todo.todoDate} ${todo.todoTime}"),
                        LocalDateTimeExtension.now()
                    ) >= 1
                ) {
                    "期限切れ"
                } else if (todo.completionFlag == CompletionFlag.Completion.value) {
                    "完了"
                }else {
                    ""
                }

            view.complete.setTextColor(if (todo.completionFlag == CompletionFlag.Unfinished.value && ChronoUnit.MINUTES.between(
                    LocalDateTimeExtension.fromStringToDate("${todo.todoDate} ${todo.todoTime}"),
                    LocalDateTimeExtension.now()
                ) <= 0
            ) {
                Color.YELLOW
            } else if (todo.completionFlag == CompletionFlag.Unfinished.value && ChronoUnit.MINUTES.between(
                    LocalDateTimeExtension.fromStringToDate("${todo.todoDate} ${todo.todoTime}"),
                    LocalDateTimeExtension.now()
                ) >= 1
            ) {
                Color.RED
            } else if (todo.completionFlag == CompletionFlag.Completion.value) {
                Color.GREEN
            }else {
                Color.YELLOW
            })

            var hour: String
            var min: String
            val tmpTime = todo.todoTime.split(":")
            min = tmpTime[1]

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
