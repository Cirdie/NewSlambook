package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivityPersonalDetails2Binding
import java.util.*

class PersonalDetails2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalDetails2Binding
    private lateinit var slambookEntry: SlambookEntry
    private var selectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDetails2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        slambookEntry = intent.getSerializableExtra("slambookEntry") as? SlambookEntry
            ?: SlambookEntry()

        binding.titleText.text = "Hello ${slambookEntry.firstName}!"

        setupSpinners()
        setupGenderSelection()

        binding.continueButton.setOnClickListener { validateInputs() }
    }

    private fun setupSpinners() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (1900..currentYear).reversed().toList().map { it.toString() }
        val months = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val days = (1..31).toList().map { it.toString() }

        // Year Spinner
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.yearSpinner.adapter = yearAdapter

        // Month Spinner
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.monthSpinner.adapter = monthAdapter

        // Day Spinner
        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.daySpinner.adapter = dayAdapter

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
        slambookEntry.gender = gender
        binding.maleContainer.setBackgroundResource(
            if (gender == "Male") R.drawable.gender_male_selected_background else R.drawable.gender_container_background
        )
        binding.femaleContainer.setBackgroundResource(
            if (gender == "Female") R.drawable.gender_female_selected_background else R.drawable.gender_container_background
        )
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

            slambookEntry.birthDate = Triple(selectedYear, selectedMonth, selectedDay)
            slambookEntry.age = age
            binding.ageInput.setText(age.toString())
        }
    }

    private fun validateInputs() {
        val weight = binding.weightInput.text.toString().trim()
        val height = binding.heightInput.text.toString().trim()

        if (selectedGender == null) {
            showErrorDialog("Please select a gender.")
            return
        }
        if (weight.isEmpty()) {
            binding.weightInput.error = "Weight is required"
            return
        }
        if (height.isEmpty()) {
            binding.heightInput.error = "Height is required"
            return
        }
        if (binding.ageInput.text.isNullOrEmpty()) {
            showErrorDialog("Please select a valid birthdate to calculate age.")
            return
        }

        slambookEntry.weight = weight
        slambookEntry.height = height
        proceedToNextStep()
    }

    private fun proceedToNextStep() {
        val intent = Intent(this, PersonalDetails3Activity::class.java).apply {
            putExtra("slambookEntry", slambookEntry)
        }
        startActivity(intent)
        finish()
    }

    private fun showErrorDialog(message: String) {
        // You can customize this method or use a dialog library
        // For simplicity, it's just showing an error here
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
