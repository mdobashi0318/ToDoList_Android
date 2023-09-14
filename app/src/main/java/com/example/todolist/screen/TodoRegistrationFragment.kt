package com.example.todolist.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTodoRegistrationBinding
import com.example.todolist.model.ToDoModel
import com.example.todolist.viewmodel.RegistrationViewModel
import com.example.todolist.other.CompletionFlag
import com.example.todolist.other.Mode
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class TodoRegistrationFragment : Fragment() {

    private val viewModel: RegistrationViewModel by viewModels()

    private lateinit var binding: FragmentTodoRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentTodoRegistrationBinding>(
            inflater,
            R.layout.fragment_todo_registration,
            container,
            false
        )
        CoroutineScope(Dispatchers.Main).launch {
            setObserver()
            setRegisterButton()
        }



        (activity as AppCompatActivity).supportActionBar?.title = modeMessage(
            resources.getString(R.string.registration_title_add),
            resources.getString(R.string.registration_title_edit)
        )


        binding.timePickerButton.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(viewModel.hour)
                    .setMinute(viewModel.min)
                    .setTitleText("時間を選択してください")
                    .build()

            picker.addOnPositiveButtonClickListener {
                viewModel.setTime(picker.hour, picker.minute)
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
                viewModel.setData(df.format(picker.selection))
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
                binding.timeTextView.text.toString().isEmpty()
            ) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("未入力の箇所があります")
                    .setNegativeButton(R.string.closeButton, null)
                    .setCancelable(false)
                    .show()
                return@setOnClickListener
            }

            // Todoを追加、または更新する
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(modeMessage("Todoを登録しますか？", "Todoを更新しますか？"))
                .setPositiveButton(R.string.yesButton) { _, _ ->
                    CoroutineScope(Dispatchers.Main).launch {
                        if (viewModel.getMode == Mode.Add) {
                            addTodo()
                        } else {
                            updateTodo()
                        }
                    }

                }
                .setNegativeButton(R.string.noButton, null)
                .show()
        }
    }

    /**
     * Todoを新規作成する
     */
    private suspend fun addTodo() {
        val format = SimpleDateFormat("MMddHHmmS")
        viewModel.add(
            requireContext(),
            ToDoModel(
                format.format(Date()).toString(),
                binding.titleEditText.text.toString(),
                binding.dateTextView.text.toString(),
                binding.timeTextView.text.toString(),
                binding.detailEditText.text.toString(),
                CompletionFlag.Unfinished.value
            )
        ) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("登録しました")
                .setPositiveButton(R.string.closeButton) { _, _ ->
                    this.findNavController()
                        .navigate(R.id.action_todoRegistrationFragment_to_tabFragment)
                }
                .setCancelable(false)
                .show()
        }
    }

    /**
     * Todoを更新する
     */
    private suspend fun updateTodo() {
        viewModel.update(
            requireContext(),
            ToDoModel(
                viewModel.getCreateTime,
                binding.titleEditText.text.toString(),
                binding.dateTextView.text.toString(),
                binding.timeTextView.text.toString(),
                binding.detailEditText.text.toString(),
                CompletionFlag.Unfinished.value
            )
        ) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("更新しました")
                .setPositiveButton(R.string.closeButton) { _, _ ->
                    view?.findNavController()
                        ?.navigate(
                            TodoRegistrationFragmentDirections.actionTodoRegistrationFragmentToTodoDetailFragment(
                                viewModel.getCreateTime
                            )
                        )
                }
                .setCancelable(false)
                .show()
        }


    }

    /**
     * Observerをセットする
     */
    private suspend fun setObserver() {
        val args: TodoRegistrationFragmentArgs by navArgs()
        viewModel.setModel(args.createTime)

        viewModel.toDoName.observe(viewLifecycleOwner) {
            binding.titleEditText.setText(it)
        }
        viewModel.toDoDetail.observe(viewLifecycleOwner) {
            binding.detailEditText.setText(it)
        }
        viewModel.todoDate.observe(viewLifecycleOwner) {
            binding.dateTextView.text = it
        }
        viewModel.todoTime.observe(viewLifecycleOwner) {
            binding.timeTextView.text = it
        }
    }

    /**
     * modeによって設定したテキストを返す
     * @param addMessage modeがAddの時に返すテキスト
     * @param editMessage modeがEditの時に返すテキスト
     */
    private fun modeMessage(addMessage: String, editMessage: String): String {
        return if (viewModel.getMode == Mode.Add) addMessage else editMessage
    }

}