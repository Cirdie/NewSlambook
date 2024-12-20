package com.example.slmabookfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.slmabookfinal.databinding.FragmentAddFavoriteDialogBinding

class AddFavoriteDialogFragment(
    private val favorite: Favorite,
    private val iconResId: Int,
    private val onFavoriteAdded: (Favorite) -> Unit
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

        binding.dialogTitle.text = "Add to ${favorite.category}"
        binding.dialogIcon.setImageResource(iconResId)

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.addFavoriteButton.setOnClickListener {
            val favoriteItem = binding.favoriteItemInput.text.toString().trim()

            if (favoriteItem.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a favorite item", Toast.LENGTH_SHORT).show()
            } else {
                favorite.items.add(favoriteItem)
                onFavoriteAdded(favorite)
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
