package com.example.slmabookfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.slmabookfinal.databinding.FragmentIconSelectionDialogBinding

class IconSelectionDialogFragment(
    private val icons: List<Int>,
    private val onIconSelected: (Int) -> Unit
) : DialogFragment() {

    private lateinit var binding: FragmentIconSelectionDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIconSelectionDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iconRecyclerView.layoutManager = GridLayoutManager(context, 4)
        val adapter = IconAdapter(icons) { selectedIcon ->
            onIconSelected(selectedIcon)
            dismiss()
        }
        binding.iconRecyclerView.adapter = adapter
    }

    companion object {
        const val TAG = "IconSelectionDialog"
    }
}
