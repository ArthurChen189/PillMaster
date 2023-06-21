package com.ece452.pillmaster.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

// TODO consider renaming this so as to serve for both uploading and retrieving
// TODO pill info to and from the database.
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class PillAddPageViewModel @Inject constructor(): ViewModel() {

    data class Pill(
        val name: String,
        val isTaken: Boolean,
        val startDate: String,
        val endDate: String,
        // Add more fields as needed
    )

    // TODO Static list for testing purpose.
    // This should be replaced by medicineList, which is dynamically fetched from the
    // backend. Each item in the list should at least contain
    // pill_name, start, end date, isTakenToday, etc.
    val testList = mutableStateOf(mutableListOf<Pill>())
    init {
        testList.value = mutableListOf(
            Pill("Aspirin", false, "2023-06-17", "2023-06-30"),
            Pill("Vitamin D", false, "2023-06-20", "2023-06-25"),
            Pill("Antibiotics", false, "2023-06-11", "2023-06-20"),
        )
        sortPillsByDate(testList.value)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sortPillsByDate(testList: MutableList<Pill>) {
        val currentDate = LocalDate.now()

        // Partition the pills into currently-taking and future pills
        val (currentPills, futurePills) = testList.partition { pill ->
            val startDate = LocalDate.parse(pill.startDate)
            val endDate = LocalDate.parse(pill.endDate)
            currentDate in startDate..endDate
        }

        // Clear the original list and add the current and future pills in the modified order
        testList.clear()
        testList.addAll(currentPills)
        testList.addAll(futurePills)
    }

    // submit a new pill
    fun newPillSubmit(
        pillName: (String),
        direction: (String),
        reminderTime: (String),
        startDate: (String),
        endDate: (String),
        selectedOption: (String),
        isChecked: (Boolean)) {

    }

    // get all Caregivers of current Carereceiver
    // return a List<String>
     fun getCareGivers() {

    }

    // TODO fetch medicines from database into the medicineList.
    fun fetchMedicineData() {

    }
}