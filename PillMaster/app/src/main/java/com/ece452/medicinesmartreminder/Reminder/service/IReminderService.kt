package com.ece452.medicinesmartreminder.Reminder.service

import com.ece452.medicinesmartreminder.Reminder.model.ReminderResponse
import retrofit2.http.GET

interface IReminderService {

    @GET("categories.php")
    //async network
    suspend fun getAllReminders(): ReminderResponse
}