package com.example.todolist.screen

import android.opengl.Visibility
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTodoDetailBinding
import com.example.todolist.other.CompletionFlag
import com.example.todolist.viewmodel.TodoDetailViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoDetailFragment : Fragment() {
    private lateinit var binding: FragmentTodoDetailBinding

    private val viewModel: TodoDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        val createTime: String = if (requireArguments().getString("createTime") != null) {
            requireArguments().getString("createTime")!!
        } else {
            val args: TodoDetailFragmentArgs by navArgs()
            args.createTime
        }

        binding = DataBindingUtil.inflate<FragmentTodoDetailBinding>(
            inflater,
            R.layout.fragment_todo_detail,
            container,
            false
        )


        CoroutineScope(Dispatchers.Main).launch {
            viewModel.find(createTime)
            binding.titleTextView.text = viewModel.model.toDoName
            binding.dateTextView.text = viewModel.model.todoDate + "\n" + viewModel.model.todoTime
            binding.detailTextView.text = viewModel.model.toDoDetail
            if (viewModel.model.toDoDetail.isEmpty()) {
                binding.detailCardView.visibility = View.GONE
            }

            binding.completeSwitch.isChecked =
                CompletionFlag.getCompletionFlag(viewModel.model.completionFlag)
            binding.completeSwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateFlag(requireContext(), isChecked)
            }
        }
        return binding.root
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
                        viewModel.model.createTime
                    )
                )
                true
            }
            R.id.detail_delete -> {
                // Todoを削除する
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Todoを削除しますか?")
                    .setPositiveButton(R.string.deleteButton) { _, _ ->
                            viewModel.delete(requireContext()) {
                                MaterialAlertDialogBuilder(requireContext())
                                    .setTitle("削除しました")
                                    .setPositiveButton(R.string.closeButton) { _, _ ->
                                        view?.findNavController()
                                            ?.navigate(R.id.action_todoDetailFragment_to_tabFragment)
                                    }
                                    .setCancelable(false)
                                    .show()
                        }
                    }
                    .setNegativeButton(R.string.cancelButton, null)
                    .setCancelable(false)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}