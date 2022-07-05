package com.cidra.hologram_beta.di

import com.cidra.hologram_beta.data.repository.FirebaseRepositoryImpl
import com.cidra.hologram_beta.data.repository.PreferencesRepositoryImpl
import com.cidra.hologram_beta.domain.repository.FirebaseRepository
import com.cidra.hologram_beta.domain.repository.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {
    @Binds
    abstract fun provideFirebaseRepository(
        firebaseRepositoryImpl: FirebaseRepositoryImpl
    ): FirebaseRepository

    @Binds
    abstract fun providePreferencesRepository(
        preferencesRepositoryImpl: PreferencesRepositoryImpl
    ): PreferencesRepository

}
