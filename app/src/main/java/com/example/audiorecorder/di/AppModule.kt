package com.example.audiorecorder.di

import com.example.audiorecorder.main.domain.GetNextAudioUiStateUseCase
import com.example.audiorecorder.main.domain.GetNextAudioUiStateUseCaseImpl
import com.example.audiorecorder.audio.AudioPlayer
import com.example.audiorecorder.audio.AudioPlayerImpl
import com.example.audiorecorder.audio.AudioRecorder
import com.example.audiorecorder.audio.AudioRecorderImpl
import com.example.audiorecorder.main.domain.HandleFilesUseCase
import com.example.audiorecorder.main.domain.HandleFilesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
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
        fun bindAudioPlayer(useCase: AudioPlayerImpl): AudioPlayer

        @Binds
        @Singleton
        fun bindGetNextAudioUiStateUseCase(useCase: GetNextAudioUiStateUseCaseImpl): GetNextAudioUiStateUseCase

        @Binds
        @Singleton
        fun bindHandleFilesUseCase(useCase: HandleFilesUseCaseImpl): HandleFilesUseCase
    }

    companion object{
        @Provides
        @AudioFileName
        fun audioFileName(): String = "rc_audio.pcm"
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AudioFileName