package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivityPersonalDetails2Binding
import com.example.slmabookfinal.utils.ProgressDialog
import java.util.*

class PersonalDetails2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalDetails2Binding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var personalDetails: PersonalDetails
    private var selectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDetails2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the personalDetails object from the Intent
        personalDetails = intent.getSerializableExtra("personalDetails") as? PersonalDetails
            ?: PersonalDetails() // Initialize if missing

        // Set the welcome message using the name
        binding.titleText.text = "Hello ${personalDetails.firstName}!"

        progressDialog = ProgressDialog(this)

        setupGenderSelection()
        setupBirthdateSelection()

        binding.continueButton.setOnClickListener {
            validateInputs()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupGenderSelection() {
        binding.maleContainer.setOnClickListener {
            selectGender("Male")
        }

        binding.femaleContainer.setOnClickListener {
            selectGender("Female")
        }
    }

    private fun selectGender(gender: String) {
        selectedGender = gender
        personalDetails.gender = gender
        binding.maleContainer.setBackgroundResource(
            if (gender == "Male") R.drawable.gender_male_selected_background else R.drawable.gender_container_background
        )
        binding.femaleContainer.setBackgroundResource(
            if (gender == "Female") R.drawable.gender_female_selected_background else R.drawable.gender_container_background
        )
    }

    private fun setupBirthdateSelection() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (1900..currentYear).reversed().toList().map { it.toString() }
        val months = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val days = (1..31).toList().map { it.toString() }

        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.monthSpinner.adapter = monthAdapter

        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.daySpinner.adapter = dayAdapter

        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.yearSpinner.adapter = yearAdapter

        val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                calculateAge()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.yearSpinner.onItemSelectedListener = onItemSelectedListener
        binding.monthSpinner.onItemSelectedListener = onItemSelectedListener
        binding.daySpinner.onItemSelectedListener = onItemSelectedListener
    }

    private fun calculateAge() {
        val selectedYear = binding.yearSpinner.selectedItem.toString().toIntOrNull()
        val selectedMonth = binding.monthSpinner.selectedItemPosition + 1
        val selectedDay = binding.daySpinner.selectedItem.toString().toIntOrNull()

        if (selectedYear != null && selectedDay != null) {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH) + 1
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            var age = currentYear - selectedYear
            if (currentMonth < selectedMonth || (currentMonth == selectedMonth && currentDay < selectedDay)) {
                age--
            }

            personalDetails.birthDate = Triple(selectedYear, selectedMonth, selectedDay)
            personalDetails.age = age
            binding.ageInput.setText(age.toString())
        }
    }

    private fun validateInputs() {
        val weight = binding.weightInput.text.toString().trim()
        val height = binding.weightInput.text.toString().trim()

        when {
            selectedGender == null -> {
                showErrorDialog("Please select your gender")
            }
            binding.ageInput.text.isNullOrEmpty() -> {
                binding.ageInput.error = "Please provide a valid age"
                binding.ageInput.requestFocus()
            }
            weight.isEmpty() -> {
                binding.weightInput.error = "Please enter your weight"
                binding.weightInput.requestFocus()
            }
            height.isEmpty() -> {
                binding.heightInput.error = "Please enter your height"
                binding.heightInput.requestFocus()
            }
            else -> {
                personalDetails.weight = weight
                personalDetails.height = height
                navigateToNextStep()
            }
        }
    }

    private fun navigateToNextStep() {
        progressDialog.show(ProgressDialog.DialogType.PROGRESS, "Proceeding to the next step...")

        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
            val intent = Intent(this, PersonalDetails3Activity::class.java)
            intent.putExtra("personalDetails", personalDetails)
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun showErrorDialog(message: String) {
        progressDialog.show(ProgressDialog.DialogType.ERROR, message)
        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
        }, 2000)
    }
}
