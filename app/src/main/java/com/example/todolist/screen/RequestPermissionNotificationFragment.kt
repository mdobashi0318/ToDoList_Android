package com.example.todolist.screen

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.todolist.R
import com.example.todolist.databinding.FragmentRequestPermissionNotificationBinding

class RequestPermissionNotificationFragment : Fragment() {

    private lateinit var binding: FragmentRequestPermissionNotificationBinding

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _ ->
            view?.findNavController()
                ?.navigate(RequestPermissionNotificationFragmentDirections.actionRequestPermissionNotificationFragmentToTabFragment())
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentRequestPermissionNotificationBinding>(
            inflater,
            R.layout.fragment_request_permission_notification,
            container,
            false
        )

        binding.requestPermissionNotificationButton.setOnClickListener {
            requestPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
        return binding.root
    }
}