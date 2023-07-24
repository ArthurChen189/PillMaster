package com.ece452.pillmaster.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// Dependency injection of Firebase backend services
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
  // Authentication
  @Provides fun auth(): FirebaseAuth = Firebase.auth

  // Non-relational Cloud Database
  @Provides fun firestore(): FirebaseFirestore = Firebase.firestore
}