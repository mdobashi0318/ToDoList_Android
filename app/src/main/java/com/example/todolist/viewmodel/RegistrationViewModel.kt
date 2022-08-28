package com.example.todolist.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.extensions.addFirstZero
import com.example.todolist.model.ToDoModel
import com.example.todolist.model.ToDoModel1
import com.example.todolist.other.Mode
import com.example.todolist.other.Notification
import com.example.todolist.screen.TodoApplication
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegistrationViewModel() : ViewModel() {

    private val dao = TodoApplication.database.todoDao()

    private var createTime: String = ""
    val getCreateTime: String get() = createTime

    val toDoName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val todoDate: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val todoTime: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val toDoDetail: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private var mode: Mode = Mode.Add

    val getMode: Mode get() = mode

    private var time: MutableMap<String, Int> = mutableMapOf("hour" to 0, "min" to 0)

    var hour: Int
        get() = time["hour"] ?: 0
        set(value) {
            time["hour"] = value
        }

    var min: Int
        get() = time["min"] ?: 0
        set(value) {
            time["min"] = value
        }

    suspend fun setModel(createTime: String?) {
        createTime?.let { createTime: String ->
            this.createTime = createTime
            val model = dao.getTodo(createTime)

            mode = Mode.Edit
            toDoName.value = model.toDoName
            todoDate.value = model.todoDate
            toDoDetail.value = model.toDoDetail

            val tmpTime = model.todoTime.split(":")
            hour = tmpTime[0].toInt()
            min = tmpTime[1].toInt()
            todoTime.value = "${this.hour}:${this.min.toString().addFirstZero()}"
        }
    }

    suspend fun add(context: Context, todo: ToDoModel1, success: () -> Unit) {
        dao.add(
            todo
        )
        Notification.setNotification(
            context,
            todo.todoDate,
            todo.todoTime,
            todo.createTime,
            todo.toDoName
        )
        success()
    }

    suspend fun update(context: Context, todo: ToDoModel1, success: () -> Unit) {
        dao.update(
            todo
        )
        Notification.setNotification(
            context,
            todo.todoDate,
            todo.todoTime,
            todo.createTime,
            todo.toDoName
        )
        success()
    }

    fun setData(date: String) {
        todoDate.value = date
    }

    fun setTime(hour: Int, min: Int) {
        this.hour = hour
        this.min = min
        todoTime.value = "${this.hour}:${this.min.toString().addFirstZero()}"
    }

}
