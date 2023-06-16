package com.ece452.pillmaster.repository;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\n"}, d2 = {"Lcom/ece452/pillmaster/repository/ReminderRepository;", "Lcom/ece452/pillmaster/repository/IReminderRepository;", "service", "Lcom/ece452/pillmaster/service/IReminderService;", "(Lcom/ece452/pillmaster/service/IReminderService;)V", "getService", "()Lcom/ece452/pillmaster/service/IReminderService;", "getAllReminders", "Lcom/ece452/pillmaster/model/ReminderResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ReminderRepository implements com.ece452.pillmaster.repository.IReminderRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.ece452.pillmaster.service.IReminderService service = null;
    
    @javax.inject.Inject()
    public ReminderRepository(@org.jetbrains.annotations.NotNull()
    com.ece452.pillmaster.service.IReminderService service) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ece452.pillmaster.service.IReminderService getService() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public java.lang.Object getAllReminders(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.ece452.pillmaster.model.ReminderResponse> continuation) {
        return null;
    }
}