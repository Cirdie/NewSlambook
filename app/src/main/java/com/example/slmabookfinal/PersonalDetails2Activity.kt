package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.slmabookfinal.databinding.ActivityPersonalDetails2Binding
import java.util.*
import java.text.DecimalFormat


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
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val years = (1980..currentYear).reversed().toList().map { it.toString() }
        val months = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        // Year Spinner
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.yearSpinner.adapter = yearAdapter

        // Month Spinner
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.monthSpinner.adapter = monthAdapter

        // Day Spinner
        updateDaysSpinner(currentYear, currentMonth)

        val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedYear = binding.yearSpinner.selectedItem.toString().toIntOrNull()
                val selectedMonth = binding.monthSpinner.selectedItemPosition
                if (selectedYear != null) {
                    updateDaysSpinner(selectedYear, selectedMonth)
                }
                calculateAge()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.yearSpinner.onItemSelectedListener = onItemSelectedListener
        binding.monthSpinner.onItemSelectedListener = onItemSelectedListener

        // Weight Spinner
        val weightOptions = (30..150).toList().map { "$it kg" }
        val weightAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, weightOptions)
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.weightSpinner.adapter = weightAdapter

        val decimalFormat = DecimalFormat("#.#")  // Format to one decimal place
        val heightOptions = (100..250 step 5)  // Generate heights from 100cm to 250cm in steps of 5cm
            .map { it / 30.48 }  // Convert cm to feet (floating point)
            .map { "${decimalFormat.format(it)} ft" }  // Format to one decimal place with "ft"

        val heightAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, heightOptions)
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.heightSpinner.adapter = heightAdapter

    }

    private fun updateDaysSpinner(year: Int, month: Int) {
        val daysInMonth = getDaysInMonth(year, month)
        val days = (1..daysInMonth).map { it.toString() }

        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.daySpinner.adapter = dayAdapter

        // Exclude future days in the current month and year
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        if (year == currentYear && month == currentMonth) {
            val validDays = (1..currentDay).map { it.toString() }
            val validDayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, validDays)
            validDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.daySpinner.adapter = validDayAdapter
        }
    }

    private fun getDaysInMonth(year: Int, month: Int): Int {
        return when (month) {
            1 -> if (isLeapYear(year)) 29 else 28 // February
            3, 5, 8, 10 -> 30 // April, June, September, November
            else -> 31 // Other months
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
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
        val selectedWeight = binding.weightSpinner.selectedItem.toString().trim()
        val selectedHeight = binding.heightSpinner.selectedItem.toString().trim()

        if (selectedGender == null) {
            showErrorDialog("Please select a gender.")
            return
        }
        if (selectedWeight.isEmpty()) {
            showErrorDialog("Please select a weight.")
            return
        }
        if (selectedHeight.isEmpty()) {
            showErrorDialog("Please select a height.")
            return
        }
        if (binding.ageInput.text.isNullOrEmpty()) {
            showErrorDialog("Please select a valid birthdate to calculate age.")
            return
        }

        slambookEntry.weight = selectedWeight
        slambookEntry.height = selectedHeight

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
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
