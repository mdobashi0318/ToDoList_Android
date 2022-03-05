package com.example.todolist.screen

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.todolist.other.Mode
import com.example.todolist.R
import com.example.todolist.model.ToDoModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.activity_todo_registration.*
import java.lang.String.format
import java.sql.Date
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant.now
import java.time.LocalDate.now
import java.util.*


class TodoRegistrationActivity : AppCompatActivity() {

    var mode: Mode = Mode.Add

    private lateinit var todo: ToDoModel

    /* TimePickerの初期値にセットする */
    private var time: Array<Int> = arrayOf<Int>(0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_registration)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setIntentDate()
        setRegisterButton()


        var hour: Int = time[0]
        var minute: Int = time[1]

        timePickerButton.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(hour)
                    .setMinute(minute)
                    .setTitleText("時間を選択してください")
                    .build()

            picker.addOnPositiveButtonClickListener {
                hour = picker.hour
                minute = picker.minute
                timeTextView.text = "${hour}:${minute}"

            }
            picker.show(supportFragmentManager, "timePicker")
        }

        datePickerButton.setOnClickListener {
            val picker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("日付を選択してください")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()


            picker.addOnPositiveButtonClickListener {
                val df = SimpleDateFormat("yyyy/MM/dd")
                dateTextView.text = df.format(picker.selection)
            }
            picker.show(supportFragmentManager, "datePicker")
        }
    }


    /**
     * 登録/更新ボタンの設定をする
     */
    private fun setRegisterButton() {
        registerButton.text = modeMessage("登録", "更新")

        registerButton.setOnClickListener {
            // 入力されているかのチェック
            if (titleEditText.text.toString().isEmpty() ||
                dateTextView.text.toString().isEmpty() ||
                timeTextView.text.toString().isEmpty() ||
                detailEditText.text.toString().isEmpty()
            ) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("未入力の箇所があります")
                    .setNegativeButton(R.string.closeButton, null)
                    .show()
                return@setOnClickListener
            }

            // Todoを追加、または更新する
            MaterialAlertDialogBuilder(this)
                .setTitle(modeMessage("Todoを登録しますか？", "Todoを更新しますか？"))
                .setPositiveButton(R.string.yesButton) { _, _ ->
                    if (mode == Mode.Add) {
                        addTodo()
                    } else {
                        updateTodo()
                    }
                }
                .setNegativeButton(R.string.noButton, null)
                .show()
        }
    }

    /**
     * Todoを新規作成する
     */
    private fun addTodo() {
        ToDoModel().add(
            applicationContext,
            titleEditText.text.toString(),
            dateTextView.text.toString(),
            timeTextView.text.toString(),
            detailEditText.text.toString()
        ) {
            MaterialAlertDialogBuilder(this)
                .setTitle("登録しました")
                .setPositiveButton(R.string.closeButton) { _, _ ->
                    finish()
                }
                .show()
        }
    }

    /**
     * Todoを更新する
     */
    private fun updateTodo() {
        ToDoModel().update(
            applicationContext,
            titleEditText.text.toString(),
            dateTextView.text.toString(),
            timeTextView.text.toString(),
            detailEditText.text.toString(),
            todo.createTime
        ) {
            MaterialAlertDialogBuilder(this)
                .setTitle("更新しました")
                .setPositiveButton(R.string.closeButton) { _, _ ->
                    finish()
                }
                .show()
        }
    }

    /**
     * intentにデータ持っていたらtodoを検索し、テキストにセットする
     */
    private fun setIntentDate() {
        intent.getStringExtra("createTime")?.let { createTime ->
            ToDoModel().find(applicationContext, createTime)?.let { result ->
                todo = result
            }
        }

        intent.getStringExtra("mode")?.let { it ->
            if (it == Mode.Edit.name) {
                mode = Mode.Edit
                titleEditText.text.append(todo.toDoName)
                dateTextView.text = todo.todoDate
                timeTextView.text = todo.todoTime
                detailEditText.text.append(todo.toDoDetail)


                if (todo.todoTime.isNotEmpty()) {
                    val tmpTime = todo.todoTime.split(":")
                    time = arrayOf<Int>(tmpTime[0].toInt(), tmpTime[1].toInt())
                }
            }
        }
    }

    /**
     * modeによって設定したテキストを返す
     * @param addMessage modeがAddの時に返すテキスト
     * @param editMessage modeがEditの時に返すテキスト
     */
    private fun modeMessage(addMessage: String, editMessage: String): String {
        return if (mode == Mode.Add) {
            addMessage
        } else {
            editMessage
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            true
        }

        return super.onOptionsItemSelected(item)
    }




}