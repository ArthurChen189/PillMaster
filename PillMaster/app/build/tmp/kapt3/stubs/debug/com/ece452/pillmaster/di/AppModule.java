package com.ece452.pillmaster.di;

import java.lang.System;

@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001:\u0001\bB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004H\u0007\u00a8\u0006\t"}, d2 = {"Lcom/ece452/pillmaster/di/AppModule;", "", "()V", "provideRetrofit", "Lretrofit2/Retrofit;", "providesReminderService", "Lcom/ece452/pillmaster/service/IReminderService;", "retrofit", "AppModuleInt", "app_debug"})
@dagger.Module()
public final class AppModule {
    
    public AppModule() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @javax.inject.Singleton()
    @dagger.Provides()
    public final retrofit2.Retrofit provideRetrofit() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @javax.inject.Singleton()
    @dagger.Provides()
    public final com.ece452.pillmaster.service.IReminderService providesReminderService(@org.jetbrains.annotations.NotNull()
    retrofit2.Retrofit retrofit) {
        return null;
    }
    
    @dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\'\u00a8\u0006\n"}, d2 = {"Lcom/ece452/pillmaster/di/AppModule$AppModuleInt;", "", "provideGetReminderUseCase", "Lcom/ece452/pillmaster/usecase/IGetReminderUseCase;", "uc", "Lcom/ece452/pillmaster/usecase/GetReminderUseCase;", "provideReminderRepository", "Lcom/ece452/pillmaster/repository/IReminderRepository;", "repo", "Lcom/ece452/pillmaster/repository/ReminderRepository;", "app_debug"})
    @dagger.Module()
    public static abstract interface AppModuleInt {
        
        @org.jetbrains.annotations.NotNull()
        @javax.inject.Singleton()
        @dagger.Binds()
        public abstract com.ece452.pillmaster.repository.IReminderRepository provideReminderRepository(@org.jetbrains.annotations.NotNull()
        com.ece452.pillmaster.repository.ReminderRepository repo);
        
        @org.jetbrains.annotations.NotNull()
        @javax.inject.Singleton()
        @dagger.Binds()
        public abstract com.ece452.pillmaster.usecase.IGetReminderUseCase provideGetReminderUseCase(@org.jetbrains.annotations.NotNull()
        com.ece452.pillmaster.usecase.GetReminderUseCase uc);
    }
}