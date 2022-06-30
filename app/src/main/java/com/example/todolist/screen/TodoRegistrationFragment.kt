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
import com.example.todolist.model.RegistrationViewModel
import com.example.todolist.model.ToDoModel
import com.example.todolist.other.Mode
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat


class TodoRegistrationFragment : Fragment() {

    private val viewModel: RegistrationViewModel by viewModels()

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
        setObserver()
        setRegisterButton()

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
                    if (viewModel.getMode == Mode.Add) {
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
            viewModel.getCreateTime
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
                .show()
        }
    }

    /**
     * Observerをセットする
     */
    private fun setObserver() {
        val args: TodoRegistrationFragmentArgs by navArgs()
        viewModel.setModel(requireContext(), args.createTime)
        val titleObserver = Observer { title: String ->
            binding.titleEditText.setText(title)
        }

        val detailObserver = Observer { detail: String ->
            binding.detailEditText.setText(detail)
        }

        val dateObserver = Observer { date: String ->
            binding.dateTextView.text = date
        }

        val timeObserver = Observer { time: String ->
            binding.timeTextView.text = time
        }

        viewModel.toDoName.observe(viewLifecycleOwner, titleObserver)
        viewModel.toDoDetail.observe(viewLifecycleOwner, detailObserver)
        viewModel.todoDate.observe(viewLifecycleOwner, dateObserver)
        viewModel.todoTime.observe(viewLifecycleOwner, timeObserver)
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