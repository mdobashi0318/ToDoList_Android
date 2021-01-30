package com.example.todolist

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_todo_registration.*
import java.util.*
import kotlin.concurrent.thread

class TodoRegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_registration)

        datePickerButton.setOnClickListener {
            DatePickerFragment(dateTextView).show(supportFragmentManager, "datePicker")
        }

        timePickerButton.setOnClickListener {
            TimePickerFragment(timeTextView).show(supportFragmentManager, "timePicker")
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
            return DatePickerDialog(activity!!, this, year, month, day)
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            // Do something with the date chosen by the user
            dateTextView.text = "${year}/${month + 1}/${day}"
        }
    }
}