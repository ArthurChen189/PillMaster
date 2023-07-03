package com.ece452.pillmaster.service

import com.ece452.pillmaster.model.Reminder
import retrofit2.http.GET

interface IReminderService {

    @GET("categories.php")
    //async network
    suspend fun getAllReminders(): List<Reminder>
}