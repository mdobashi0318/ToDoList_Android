package com.example.todolist.screen

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.todolist.other.Mode
import com.example.todolist.R
import com.example.todolist.model.ToDoModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_todo_registration.*
import java.util.*


class TodoRegistrationActivity : AppCompatActivity() {

    var mode: Mode = Mode.Add

    lateinit var todo: ToDoModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_registration)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        datePickerButton.setOnClickListener {
            DatePickerFragment(dateTextView).show(supportFragmentManager, "datePicker")
        }

        timePickerButton.setOnClickListener {
            TimePickerFragment(timeTextView).show(supportFragmentManager, "timePicker")
        }

        setIntentDate()
        setRegisterButton()
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

        intent.getStringExtra("mode")?.let {
            if (it == Mode.Edit.name) {
                mode = Mode.Edit
                titleEditText.text.append(todo.toDoName)
                dateTextView.text = todo.todoDate
                timeTextView.text = todo.todoTime
                detailEditText.text.append(todo.toDoDetail)
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

    /**
     * TimePickerを設定する
     */
    class TimePickerFragment(private var timeTextView: TextView) : DialogFragment(),
        TimePickerDialog.OnTimeSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current time as the default values for the picker
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // Create a new instance of TimePickerDialog and return it
            return TimePickerDialog(activity, this, hour, minute, true)
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            // Do something with the time chosen by the user
            timeTextView.text = "${hourOfDay}:${minute}"
        }


    }

    /**
     * DatePickerを設定する
     */
    class DatePickerFragment(private var dateTextView: TextView) : DialogFragment(),
        DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current date as the default date in the picker
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(requireActivity(), this, year, month, day)
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            // Do something with the date chosen by the user
            dateTextView.text = "${year}/${month + 1}/${day}"
        }
    }
}