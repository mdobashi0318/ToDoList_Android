package com.example.todolist.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.extensions.addFirstZero
import com.example.todolist.model.ToDoModel
import com.example.todolist.other.Mode
import com.example.todolist.other.Notification
import com.example.todolist.screen.TodoApplication
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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


    init {
        initTime()
    }

    /**
     * Todoがあれば各プロパティにセットする
     */
    suspend fun setModel(createTime: String?) {
        createTime?.let { createTime: String ->
            this.createTime = createTime
            val model = dao.getTodo(createTime)

            mode = Mode.Edit
            toDoName.value = model.toDoName
            todoDate.value = model.todoDate
            toDoDetail.value = model.toDoDetail

            setTime(model.todoTime)
        }
    }

    suspend fun add(context: Context, todo: ToDoModel, success: () -> Unit) {
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

    suspend fun update(context: Context, todo: ToDoModel, success: () -> Unit) {
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

    /**
     * Pickerで設定した日付を格納する
     */
    fun setData(date: String) {
        todoDate.value = date
    }


    /**
     * 引数の時間を格納する
     */
    fun setTime(hour: Int, min: Int) {
        this.hour = hour
        this.min = min
        todoTime.value = "${this.hour.toString().addFirstZero()}:${this.min.toString().addFirstZero()}"
    }

    /**
     * 引数の時間を格納する
     */
    private fun setTime(time: String) {
        val tmpTime = time.split(":")
        setTime(tmpTime[0].toInt(), tmpTime[1].toInt())
    }


    /**
     * 画面開いた時の日付と時間の初期値としてセットする
     */
    private fun initTime() {
        val localDateTime = LocalDateTime.now()

        val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val date = dateFormat.format(localDateTime)
        todoDate.value = date

        val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
        val time = timeFormat.format(localDateTime)
        setTime(time)
    }


}
