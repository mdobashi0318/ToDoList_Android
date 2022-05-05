package com.example.todolist.screen

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTodoDetailBinding
import com.example.todolist.model.ToDoModel
import com.example.todolist.other.Mode
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TodoDetailFragment : Fragment() {
    private lateinit var binding: FragmentTodoDetailBinding
    private lateinit var model: ToDoModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val args: TodoDetailFragmentArgs by navArgs()
        binding = DataBindingUtil.inflate<FragmentTodoDetailBinding>(
            inflater,
            R.layout.fragment_todo_detail,
            container,
            false
        )
        ToDoModel().find(requireContext(), args.createTime)?.let {
            model = it
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.titleTextView.text = model.toDoName
        binding.dateTextView.text = model.todoDate
        binding.detailLabel.text = model.toDoDetail
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.detail_edit -> {
                // Todo編集画面に遷移する
                view?.findNavController()?.navigate(
                    TodoDetailFragmentDirections.actionTodoDetailFragmentToTodoRegistrationFragment(
                        model.createTime
                    )
                )
                true
            }
            R.id.detail_delete -> {
                // Todoを削除する
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Todoを削除しますか?")
                    .setPositiveButton(R.string.deleteButton) { _, _ ->
                        ToDoModel().delete(requireContext(), model.createTime) {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("削除しました")
                                .setPositiveButton(R.string.closeButton) { _, _ ->
                                    view?.findNavController()
                                        ?.navigate(R.id.action_todoDetailFragment_to_todoListFragment)
                                }
                                .show()
                        }
                    }
                    .setNegativeButton(R.string.cancelButton, null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}