package com.example.slambookfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.slmabookfinal.databinding.FragmentAddFavoriteDialogBinding

class AddFavoriteDialogFragment(
    private val favorite: Favorite, // Pass the Favorite object
    private val iconResId: Int,     // Pass the icon resource ID
    private val onFavoriteAdded: (Favorite) -> Unit // Callback for when an item is added
) : DialogFragment() {

    private var _binding: FragmentAddFavoriteDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddFavoriteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set category title and icon
        binding.dialogTitle.text = "Add to ${favorite.category}"
        binding.dialogIcon.setImageResource(iconResId)

        // Close dialog
        binding.closeButton.setOnClickListener {
            dismiss()
        }

        // Add favorite item
        binding.addFavoriteButton.setOnClickListener {
            val favoriteItem = binding.favoriteItemInput.text.toString().trim()

            if (favoriteItem.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a favorite item", Toast.LENGTH_SHORT).show()
            } else {
                favorite.items.add(favoriteItem) // Add item to the category's list
                onFavoriteAdded(favorite) // Callback to update the activity
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
