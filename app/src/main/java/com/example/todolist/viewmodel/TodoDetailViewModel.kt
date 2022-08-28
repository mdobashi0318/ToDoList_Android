package com.example.todolist.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.model.ToDoModel1
import com.example.todolist.other.CompletionFlag
import com.example.todolist.other.Notification
import com.example.todolist.screen.TodoApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoDetailViewModel : ViewModel() {

    private val dao = TodoApplication.database.todoDao()

    lateinit var model: ToDoModel1


    suspend fun find(createTime: String) {
        model = dao.getTodo(createTime)
    }


    fun updateFlag(context: Context, flag: Boolean) {
        CoroutineScope(Dispatchers.Default).launch {
            var completionFlag = CompletionFlag.getCompletionFlag(flag)
            model.completionFlag = completionFlag.value
            dao.update(model)

            if (CompletionFlag.getCompletionFlag(completionFlag.value)) {
                Notification.cancelNotification(context, model.createTime)
            } else {
                Notification.setNotification(
                    context,
                    model.todoDate,
                    model.todoTime,
                    model.createTime,
                    model.toDoName
                )
            }
        }
    }

    fun delete(context: Context, success: () -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            dao.delete(model)
            Notification.cancelNotification(context, model.createTime)
        }
        success()
    }

}