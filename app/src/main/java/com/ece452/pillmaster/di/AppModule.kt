package com.ece452.pillmaster.di

import com.ece452.pillmaster.usecase.GetReminderUseCase
import com.ece452.pillmaster.usecase.IGetReminderUseCase
import com.ece452.pillmaster.repository.IAuthRepository
import com.ece452.pillmaster.repository.AuthRepository
import com.ece452.pillmaster.repository.IPillRepository
import com.ece452.pillmaster.repository.PillRepository
import com.ece452.pillmaster.repository.IReminderRepository
import com.ece452.pillmaster.repository.ReminderRepository
import com.ece452.pillmaster.service.IReminderService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// this is the place we tell Hilt how to make some of our dependencies
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //will convert it to string for debugging
//            .addConverterFactory(ScalarsConverterFactory.create())

        //will convert it to ReminderResponse object format

    }

    @Provides
    @Singleton
    //know how to call api
    fun providesReminderService(retrofit: Retrofit): IReminderService {
        return retrofit.create(IReminderService::class.java)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface AppModuleInt{

        @Binds
        @Singleton
        fun provideReminderRepository(repo: ReminderRepository) : IReminderRepository

        @Binds
        @Singleton
        fun providePillRepository(repo: PillRepository) : IPillRepository

        @Binds
        @Singleton
        fun provideAuthRepository(repo: AuthRepository) : IAuthRepository

        @Binds
        @Singleton
        fun provideGetReminderUseCase(uc: GetReminderUseCase) : IGetReminderUseCase


    }

}