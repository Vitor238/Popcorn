package com.vitor238.popcorn.ui.preferences.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vitor238.popcorn.databinding.FragmentChangeNameDialogBinding
import com.vitor238.popcorn.ui.viewmodel.ProfileViewModel

private const val USER_NAME = "name"

class ChangeNameDialogFragment : BottomSheetDialogFragment() {
    private var userName: String? = null
    private var _binding: FragmentChangeNameDialogBinding? = null
    private val binding: FragmentChangeNameDialogBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString(USER_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChangeNameDialogBinding.inflate(layoutInflater, container, false)

        binding.editNewName.setText(userName)

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonSave.setOnClickListener {
            val newName = binding.editNewName.text.toString()
            val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
            profileViewModel.updateFirestoreName(newName)
            profileViewModel.updateAuthName(newName)
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(name: String?) =
            ChangeNameDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_NAME, name)
                }
            }
    }
}