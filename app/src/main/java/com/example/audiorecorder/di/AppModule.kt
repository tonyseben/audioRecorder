package com.example.audiorecorder.di

import com.example.audiorecorder.messages.domain.GetNextAudioStateUseCase
import com.example.audiorecorder.messages.domain.GetNextAudioStateUseCaseImpl
import com.example.audiorecorder.messages.ui.recorder.AudioRecorder
import com.example.audiorecorder.messages.ui.recorder.AudioRecorderImpl
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
    interface AppModuleInt {

        @Binds
        @Singleton
        fun bindAudioRecorder(useCase: AudioRecorderImpl): AudioRecorder

        @Binds
        @Singleton
        fun bindGetNextAudioStateUseCase(useCase: GetNextAudioStateUseCaseImpl): GetNextAudioStateUseCase
    }

}