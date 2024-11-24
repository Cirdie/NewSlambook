package com.example.slmabookfinal

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivityPersonalDetails2Binding
import java.text.SimpleDateFormat
import java.util.*

class PersonalDetails2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalDetails2Binding
    private var selectedGender: String? = null // To store the selected gender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDetails2Binding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.maleContainer.setBackgroundResource(
            if (gender == "Male") R.drawable.gender_container_selected_background else R.drawable.gender_container_background
        )
        binding.femaleContainer.setBackgroundResource(
            if (gender == "Female") R.drawable.gender_container_selected_background else R.drawable.gender_container_background
        )
    }

    private fun setupBirthdateSelection() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (currentYear..1900).toList().map { it.toString() }
        val months = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val days = (1..31).toList().map { it.toString() }

        // Set up adapters
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.monthSpinner.adapter = monthAdapter

        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.daySpinner.adapter = dayAdapter

        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.yearSpinner.adapter = yearAdapter

        // Set item selected listeners
        val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                calculateAge()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        binding.yearSpinner.onItemSelectedListener = onItemSelectedListener
        binding.monthSpinner.onItemSelectedListener = onItemSelectedListener
        binding.daySpinner.onItemSelectedListener = onItemSelectedListener
    }


    private fun calculateAge() {
        val selectedYear = binding.yearSpinner.selectedItem.toString().toIntOrNull()
        val selectedMonth = binding.monthSpinner.selectedItemPosition + 1 // Months are 0-based
        val selectedDay = binding.daySpinner.selectedItem.toString().toIntOrNull()

        if (selectedYear != null && selectedDay != null) {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH) + 1
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            var age = currentYear - selectedYear
            if (currentMonth < selectedMonth || (currentMonth == selectedMonth && currentDay < selectedDay)) {
                age-- // Adjust for incomplete year
            }

            binding.ageInput.setText(age.toString()) // Set calculated age
        }
    }


    private fun validateInputs() {
        val weight = binding.weightInput.text.toString().trim()
        val height = binding.heightInput.text.toString().trim()

        when {
            selectedGender == null -> {
                Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show()
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
                saveDetails()
            }
        }
    }

    private fun saveDetails() {
        val gender = selectedGender
        val birthdate = "${binding.monthSpinner.selectedItem} ${binding.daySpinner.selectedItem}, ${binding.yearSpinner.selectedItem}"
        val age = binding.ageInput.text.toString()
        val weight = binding.weightInput.text.toString()
        val height = binding.heightInput.text.toString()

        Toast.makeText(this, "Details Saved: $gender, $birthdate, $age, $weight, $height", Toast.LENGTH_SHORT).show()
        // Proceed to the next step
    }
}
