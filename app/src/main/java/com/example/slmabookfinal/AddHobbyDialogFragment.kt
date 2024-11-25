package com.example.slmabookfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.slmabookfinal.databinding.FragmentAddHobbyDialogBinding

class AddHobbyDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentAddHobbyDialogBinding
    private var onHobbyAdded: ((Hobby) -> Unit)? = null
    private var selectedIconResId: Int? = null

    private val iconList = listOf(
        R.drawable.ic_movies,
        R.drawable.ic_sports,
        R.drawable.ic_music,
        R.drawable.ic_hobbies,
        R.drawable.ic_foods
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddHobbyDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeButton.setOnClickListener { dismiss() }

        setupIconRecyclerView()

        binding.addHobbyButton.setOnClickListener {
            val hobbyName = binding.hobbyNameInput.text.toString().trim()

            if (hobbyName.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a hobby name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedIconResId == null) {
                Toast.makeText(requireContext(), "Please select an icon", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            onHobbyAdded?.invoke(Hobby(hobbyName, selectedIconResId!!))
            dismiss()
        }
    }

    private fun setupIconRecyclerView() {
        binding.iconRecyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.iconRecyclerView.adapter = IconAdapter(iconList) { selectedIcon ->
            selectedIconResId = selectedIcon
        }
    }

    companion object {
        const val TAG = "AddHobbyDialogFragment"

        fun newInstance(onHobbyAdded: (Hobby) -> Unit): AddHobbyDialogFragment {
            return AddHobbyDialogFragment().apply {
                this.onHobbyAdded = onHobbyAdded
            }
        }
    }
}
