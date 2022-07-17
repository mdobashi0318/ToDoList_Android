package com.example.todolist.screen

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTabBinding
import com.example.todolist.model.ToDoModel
import com.example.todolist.other.CompletionFlag
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class TabFragment : Fragment() {

    private lateinit var tabAdapter: TabAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: FragmentTabBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentTabBinding>(
            inflater,
            R.layout.fragment_tab,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tabAdapter = TabAdapter(this)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = tabAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0) tab.text = "未完了" else tab.text = "完了"
        }.attach()
    }

}


class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) TodoListFragment(CompletionFlag.Unfinished) else TodoListFragment(
            CompletionFlag.Completion
        )
    }
}