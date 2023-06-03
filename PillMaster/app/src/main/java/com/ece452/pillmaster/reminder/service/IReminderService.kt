package com.ece452.pillmaster.reminder.service

import com.ece452.pillmaster.reminder.model.ReminderResponse
import retrofit2.http.GET

interface IReminderService {

    @GET("categories.php")
    //async network
    suspend fun getAllReminders(): ReminderResponse
}