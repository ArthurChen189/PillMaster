package com.ece452.pillmaster.viewmodel;

import java.lang.System;

@dagger.hilt.android.lifecycle.HiltViewModel()
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\r"}, d2 = {"Lcom/ece452/pillmaster/viewmodel/ReminderViewModel;", "Landroidx/lifecycle/ViewModel;", "useCase", "Lcom/ece452/pillmaster/usecase/IGetReminderUseCase;", "(Lcom/ece452/pillmaster/usecase/IGetReminderUseCase;)V", "_listofReminders", "Landroidx/compose/runtime/MutableState;", "", "Lcom/ece452/pillmaster/model/Category;", "listOfReminders", "Landroidx/compose/runtime/State;", "getListOfReminders", "()Landroidx/compose/runtime/State;", "app_debug"})
public final class ReminderViewModel extends androidx.lifecycle.ViewModel {
    private final androidx.compose.runtime.MutableState<java.util.List<com.ece452.pillmaster.model.Category>> _listofReminders = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.State<java.util.List<com.ece452.pillmaster.model.Category>> listOfReminders = null;
    
    @javax.inject.Inject()
    public ReminderViewModel(@org.jetbrains.annotations.NotNull()
    com.ece452.pillmaster.usecase.IGetReminderUseCase useCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.runtime.State<java.util.List<com.ece452.pillmaster.model.Category>> getListOfReminders() {
        return null;
    }
}