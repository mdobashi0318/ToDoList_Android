package com.example.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.FragmentTodoRegistrationBinding
import com.example.todolist.model.ToDoModel
import com.example.todolist.other.Mode
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.activity_todo_registration.*
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TodoRegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodoRegistrationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var mode: Mode = Mode.Add

    private lateinit var todo: ToDoModel

    /* TimePickerの初期値にセットする */
    private var time: Array<Int> = arrayOf<Int>(0, 0)

    private lateinit var binding: FragmentTodoRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
//        setIntentDate()
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TodoRegistrationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TodoRegistrationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    /**
     * 登録/更新ボタンの設定をする
     */
    private fun setRegisterButton() {
        binding.registerButton.text = "登録"//modeMessage("登録", "更新")

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
            todo.createTime
        ) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("更新しました")
                .setPositiveButton(R.string.closeButton) { _, _ ->
//                    finish()
                }
                .show()
        }
    }

    /**
     * intentにデータ持っていたらtodoを検索し、テキストにセットする
     */
    private fun setIntentDate() {

//        intent.getStringExtra("createTime")?.let { createTime ->
//            ToDoModel().find(applicationContext, createTime)?.let { result ->
//                todo = result
//            }
//        }
//
//        intent.getStringExtra("mode")?.let { it ->
//            if (it == Mode.Edit.name) {
//                mode = Mode.Edit
//                titleEditText.text.append(todo.toDoName)
//                dateTextView.text = todo.todoDate
//                timeTextView.text = todo.todoTime
//                detailEditText.text.append(todo.toDoDetail)
//
//
//                if (todo.todoTime.isNotEmpty()) {
//                    val tmpTime = todo.todoTime.split(":")
//                    time = arrayOf<Int>(tmpTime[0].toInt(), tmpTime[1].toInt())
//                }
//            }
//        }
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


        return super.onOptionsItemSelected(item)
    }

}