package com.ece452.pillmaster.di

import android.content.Context
import com.ece452.pillmaster.PillMasterApplication
import com.ece452.pillmaster.repository.IAuthRepository
import com.ece452.pillmaster.repository.AuthRepository
import com.ece452.pillmaster.repository.CareGiverContactRepository
import com.ece452.pillmaster.repository.CareReceiverContactRepository
import com.ece452.pillmaster.repository.IContactRepository
import com.ece452.pillmaster.repository.IPillRepository
import com.ece452.pillmaster.repository.PillRepository
import com.ece452.pillmaster.repository.IReminderRepository
import com.ece452.pillmaster.repository.IUserChatRepository
import com.ece452.pillmaster.repository.ReminderRepository
import com.ece452.pillmaster.repository.UserChatRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

// this is the place we tell Hilt how to make some of our dependencies
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): PillMasterApplication {
        return app as PillMasterApplication
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
        @Named("careReceiverContact")
        fun provideCareReceiverContactRepository(repo: CareReceiverContactRepository) : IContactRepository

        @Binds
        @Singleton
        @Named("careGiverContact")
        fun provideCareGiverContactRepository(repo: CareGiverContactRepository) : IContactRepository

        @Binds
        @Singleton
        fun provideUserChatRepository(repo: UserChatRepository) : IUserChatRepository

    }

}