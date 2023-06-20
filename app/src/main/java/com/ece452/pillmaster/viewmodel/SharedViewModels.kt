package com.ece452.pillmaster.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/*
* Create all shared viewModels here, such as to easily access them in NavHost,
* without the need to pass and receive values from a screen to another.
*  */
@RequiresApi(Build.VERSION_CODES.O)
class SharedPillDataStoreOwner : ViewModelStoreOwner {
    private val viewModelStore = ViewModelStore()

    val sharedViewModel by lazy {
        PillAddPageViewModel()
    }

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }
}

class YourViewModelStoreOwner : ViewModelStoreOwner {
    private val viewModelStore = ViewModelStore()

    val sharedViewModel by lazy {
        // Name of your viewModel.
    }

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }
}