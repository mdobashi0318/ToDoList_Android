package com.example.todolist

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_todo_registration.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.*


enum class Mode {
    Add, Edit
}

class TodoRegistrationActivity : AppCompatActivity() {

    var mode: Mode = Mode.Add

    lateinit var todo: ToDoModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_registration)

        datePickerButton.setOnClickListener {
            DatePickerFragment(dateTextView).show(supportFragmentManager, "datePicker")
        }

        timePickerButton.setOnClickListener {
            TimePickerFragment(timeTextView).show(supportFragmentManager, "timePicker")
        }

        setIntentDate()

        registerButton.text = modeMessage("登録", "更新")

        registerButton.setOnClickListener {
            if (titleEditText.text.toString().isEmpty() ||
                dateTextView.text.toString().isEmpty() ||
                timeTextView.text.toString().isEmpty() ||
                detailEditText.text.toString().isEmpty()
            ) {
                alert("未入力の箇所があります") {
                    yesButton { }
                }.show()
                return@setOnClickListener
            }

            alert(modeMessage("Todoを登録しますか？", "Todoを更新しますか？")) {
                yesButton {
                    if (mode == Mode.Add) {
                        ToDoModel().add(
                            applicationContext,
                            titleEditText.text.toString(),
                            dateTextView.text.toString(),
                            timeTextView.text.toString(),
                            detailEditText.text.toString()
                        ) {
                            alert("登録しました") {
                                yesButton {
                                    finish()
                                }
                            }.show()
                        }
                    } else {
                        ToDoModel().update(
                            applicationContext,
                            titleEditText.text.toString(),
                            dateTextView.text.toString(),
                            timeTextView.text.toString(),
                            detailEditText.text.toString(),
                            todo.createTime
                        ) {
                            alert("更新しました") {
                                yesButton {
                                    finish()
                                }
                            }.show()
                        }
                    }
                }
                negativeButton("閉じる") {}
            }.show()
        }
    }


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
                // TODO: 2021/12/20
                timeTextView.text = "時間"
                detailEditText.text.append(todo.toDoDetail)
            }
        }
    }

    private fun modeMessage(addMessage: String, editMessage: String): String {
        return if (mode == Mode.Add) {
            addMessage
        } else {
            editMessage
        }
    }


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