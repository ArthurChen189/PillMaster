package com.ece452.medicinesmartreminder.di

import com.ece452.medicinesmartreminder.Reminder.GetReminderUseCase
import com.ece452.medicinesmartreminder.Reminder.IGetReminderUseCase
import com.ece452.medicinesmartreminder.Reminder.repository.IReminderRepository
import com.ece452.medicinesmartreminder.Reminder.repository.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// this is the place we tell Hilt how to amke some of our dependencies
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Module
    @InstallIn(SingletonComponent::class)
    interface AppModuleInt{

        @Binds
        @Singleton
        fun provideReminderRepository(repo:ReminderRepository) : IReminderRepository

        @Binds
        @Singleton
        fun provideGetReminderUseCase(uc:GetReminderUseCase) : IGetReminderUseCase


    }

}