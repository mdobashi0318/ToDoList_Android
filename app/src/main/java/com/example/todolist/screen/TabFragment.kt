package com.example.todolist.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTabBinding
import com.example.todolist.other.CompletionFlag
import com.example.todolist.other.Notification
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TabFragment : Fragment() {

    private lateinit var tabAdapter: TabAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: FragmentTabBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate<FragmentTabBinding>(
            inflater,
            R.layout.fragment_tab,
            container,
            false
        )

        binding.floatingActionButton.setOnClickListener { view: View ->
            // Todo作成画面に遷移する
            view.findNavController()
                .navigate(
                    TabFragmentDirections.actionTabFragmentToTodoRegistrationFragment(
                        null
                    )
                )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = view.findViewById(R.id.pager)
        tabAdapter = TabAdapter(this)
        viewPager.adapter = tabAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "未完了"
                1 -> "期限切れ"
                else -> {
                    "完了"
                }
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }

        tabAdapter = TabAdapter(this)
        viewPager.adapter = tabAdapter
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.trash -> {
                // Todoを全件削除する
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("全件削除しますか？")
                    .setPositiveButton(R.string.deleteButton) { _, _ ->
                        CoroutineScope(Dispatchers.IO).launch {
                            Notification.cancelAllNotification(requireContext()) {
                                TodoApplication.database.todoDao().deleteAll()

                            }

                        }
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("削除しました")
                            .setPositiveButton(R.string.closeButton) { _, _ -> onResume() }
                            .show()


                    }
                    .setNegativeButton(R.string.cancelButton) { _, _ -> }
                    .show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun requestNotificationPermission() {
        val notificationPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.POST_NOTIFICATIONS
        )
        val notificationRationale = shouldShowRequestPermissionRationale(
            activity!!,
            Manifest.permission.POST_NOTIFICATIONS
        )
        if (!notificationRationale && notificationPermission != PackageManager.PERMISSION_GRANTED) {
            view?.findNavController()
                ?.navigate(TabFragmentDirections.actionTabFragmentToRequestPermissionNotificationFragment())
        }
    }

}


class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodoListFragment(CompletionFlag.Unfinished)
            1 -> TodoListFragment(CompletionFlag.Expired)
            else -> {
                TodoListFragment(CompletionFlag.Completion)
            }
        }
    }
}