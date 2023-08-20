package com.example.audiorecorder.di

import com.example.audiorecorder.record.domain.GetNextAudioStateUseCase
import com.example.audiorecorder.record.domain.GetNextAudioStateUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Module
    @InstallIn(SingletonComponent::class)
    interface AppModuleInt{

        @Binds
        @Singleton
        fun bindGetNextAudioStateUseCase(useCase: GetNextAudioStateUseCaseImpl): GetNextAudioStateUseCase
    }

}