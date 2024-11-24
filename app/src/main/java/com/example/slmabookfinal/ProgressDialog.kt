package com.example.slmabookfinal.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.example.slmabookfinal.R
import com.example.slmabookfinal.databinding.CustomProgressDialogBinding

class ProgressDialog(private val context: Context) {

    private val dialog: Dialog = Dialog(context)
    private val binding: CustomProgressDialogBinding = CustomProgressDialogBinding.inflate(LayoutInflater.from(context))

    init {
        // Set up the dialog with the custom layout
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun show(type: DialogType, message: String) {
        // Update the message
        binding.progressMessage.text = message

        // Handle view visibility based on type
        when (type) {
            DialogType.PROGRESS -> {
                binding.progressAnimation.visibility = View.VISIBLE
                binding.progressAnimation.setAnimation(R.raw.progress_animation)
                binding.progressAnimation.playAnimation()
                binding.errorIcon.visibility = View.GONE
                binding.successIcon.visibility = View.GONE
            }
            DialogType.SUCCESS -> {
                binding.progressAnimation.visibility = View.GONE
                binding.errorIcon.visibility = View.GONE
                binding.successIcon.visibility = View.VISIBLE
            }
            DialogType.ERROR -> {
                binding.progressAnimation.visibility = View.GONE
                binding.successIcon.visibility = View.GONE
                binding.errorIcon.visibility = View.VISIBLE
            }
        }

        // Show the dialog
        dialog.show()
    }

    fun dismiss() {
        // Dismiss the dialog
        dialog.dismiss()
    }

    enum class DialogType {
        PROGRESS, SUCCESS, ERROR
    }
}
