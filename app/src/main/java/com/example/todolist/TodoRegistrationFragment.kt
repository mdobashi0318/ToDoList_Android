package com.example.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.databinding.FragmentTodoRegistrationBinding
import com.example.todolist.model.ToDoModel
import com.example.todolist.other.Mode
import com.example.todolist.screen.TodoDetailFragmentDirections
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.activity_todo_registration.*
import java.text.SimpleDateFormat


class TodoRegistrationFragment : Fragment() {

    var mode: Mode = Mode.Add

    private lateinit var model: ToDoModel

    /* TimePickerの初期値にセットする */
    private var time: Array<Int> = arrayOf<Int>(0, 0)

    private lateinit var binding: FragmentTodoRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentTodoRegistrationBinding>(
            inflater,
            R.layout.fragment_todo_registration,
            container,
            false
        )
        setIntentDate()
        setRegisterButton()

        var hour: Int = time[0]
        var minute: Int = time[1]

        binding.timePickerButton.setOnClickListener {
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
            picker.show(parentFragmentManager, "timePicker")
        }

        binding.datePickerButton.setOnClickListener {
            val picker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("日付を選択してください")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()


            picker.addOnPositiveButtonClickListener {
                val df = SimpleDateFormat("yyyy/MM/dd")
                dateTextView.text = df.format(picker.selection)
            }
            picker.show(parentFragmentManager, "datePicker")
        }


        return binding.root
    }


    /**
     * 登録/更新ボタンの設定をする
     */
    private fun setRegisterButton() {
        binding.registerButton.text = modeMessage("登録", "更新")

        binding.registerButton.setOnClickListener {
            // 入力されているかのチェック
            if (binding.titleEditText.text.toString().isEmpty() ||
                binding.dateTextView.text.toString().isEmpty() ||
                binding.timeTextView.text.toString().isEmpty() ||
                binding.detailEditText.text.toString().isEmpty()
            ) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("未入力の箇所があります")
                    .setNegativeButton(R.string.closeButton, null)
                    .show()
                return@setOnClickListener
            }

            // Todoを追加、または更新する
            MaterialAlertDialogBuilder(requireContext())
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
            requireContext(),
            binding.titleEditText.text.toString(),
            binding.dateTextView.text.toString(),
            binding.timeTextView.text.toString(),
            binding.detailEditText.text.toString()
        ) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("登録しました")
                .setPositiveButton(R.string.closeButton) { _, _ ->
                    this.findNavController()
                        .navigate(R.id.action_todoRegistrationFragment_to_todoListFragment)
                }
                .show()
        }
    }

    /**
     * Todoを更新する
     */
    private fun updateTodo() {
        ToDoModel().update(
            requireContext(),
            binding.titleEditText.text.toString(),
            binding.dateTextView.text.toString(),
            binding.timeTextView.text.toString(),
            binding.detailEditText.text.toString(),
            model.createTime
        ) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("更新しました")
                .setPositiveButton(R.string.closeButton) { _, _ ->
                    view?.findNavController()
                        ?.navigate(TodoRegistrationFragmentDirections.actionTodoRegistrationFragmentToTodoDetailFragment(model.createTime))
                }
                .show()
        }
    }

    /**
     * intentにデータ持っていたらtodoを検索し、テキストにセットする
     */
    private fun setIntentDate() {
        val args: TodoRegistrationFragmentArgs by navArgs()

        args?.createTime?.let { createTime ->
            ToDoModel().find(requireContext(), createTime)?.let {
                model = it
                mode = Mode.Edit
                binding.titleEditText.text.append(model.toDoName)
                binding.dateTextView.text = model.todoDate
                binding.timeTextView.text = model.todoTime
                binding.detailEditText.text.append(model.toDoDetail)

                if (model.todoTime.isNotEmpty()) {
                    val tmpTime = model.todoTime.split(":")
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

}