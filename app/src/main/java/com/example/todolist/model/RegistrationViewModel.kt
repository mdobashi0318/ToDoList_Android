package com.example.todolist.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.other.Mode


class RegistrationViewModel() : ViewModel() {

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

    fun setModel(context: Context, createTime: String?) {
        createTime?.let { createTime: String ->
            this.createTime = createTime
            ToDoModel().find(context, createTime)?.let { todo: ToDoModel ->
                mode = Mode.Edit
                toDoName.value = todo.toDoName
                todoDate.value = todo.todoDate
                todoTime.value = todo.todoTime
                toDoDetail.value = todo.toDoDetail

                if (todo.todoTime.isNotEmpty()) {
                    val tmpTime = todo.todoTime.split(":")
                    hour = tmpTime[0].toInt()
                    min = tmpTime[1].toInt()
                }
            }
        }
    }

    fun setData(date: String) {
        todoDate.value = date
    }

    fun setTime(hour: Int, min: Int) {
        this.hour = hour
        this.min = min
        todoTime.value = "${this.hour}:${this.min}"
    }

}
