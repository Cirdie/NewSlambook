package com.example.slmabookfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.slmabookfinal.databinding.FragmentAddHobbyDialogBinding
import com.example.slmabookfinal.utils.ProgressDialog

class AddHobbyDialogFragment(
    private val onHobbyAdded: (Hobby) -> Unit
) : DialogFragment() {

    private lateinit var binding: FragmentAddHobbyDialogBinding
    private var selectedIconResId: Int = R.drawable.ic_hobbies // Default icon

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddHobbyDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.changeIconButton.setOnClickListener {
            showIconSelectionDialog()
        }

        binding.addHobbyButton.setOnClickListener {
            val hobbyName = binding.hobbyNameInput.text.toString().trim()
            if (hobbyName.isEmpty()) {
                binding.hobbyNameInput.error = "Please enter a hobby name"
                return@setOnClickListener
            }

            val progressDialog = ProgressDialog(requireContext())
            progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Adding Hobby...")

            // Simulate delay for adding hobby
            binding.addHobbyButton.postDelayed({
                progressDialog.dismiss()
                val newHobby = Hobby(hobbyName, selectedIconResId)
                onHobbyAdded(newHobby)
                Toast.makeText(context, "Hobby Added!", Toast.LENGTH_SHORT).show()
                dismiss()
            }, 1500)
        }
    }

    private fun showIconSelectionDialog() {
        // List of available icons
        val icons = listOf(
            R.drawable.ic_creative,
            R.drawable.ic_sports,
            R.drawable.ic_music,
            R.drawable.ic_foods,
            R.drawable.ic_reading
        )

        val iconSelectionDialog = IconSelectionDialogFragment(icons) { selectedIcon ->
            selectedIconResId = selectedIcon
            binding.currentIcon.setImageResource(selectedIconResId) // Update the current icon
        }
        iconSelectionDialog.show(parentFragmentManager, "IconSelectionDialog")
    }

    companion object {
        const val TAG = "AddHobbyDialogFragment"

        fun newInstance(onHobbyAdded: (Hobby) -> Unit): AddHobbyDialogFragment {
            return AddHobbyDialogFragment(onHobbyAdded)
        }
    }
}
