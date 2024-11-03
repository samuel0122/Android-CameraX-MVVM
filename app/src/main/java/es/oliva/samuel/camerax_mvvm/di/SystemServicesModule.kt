package es.oliva.samuel.camerax_mvvm.di

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SystemServicesModule {
    @Provides
    fun provideAudioManager(application: Application): AudioManager {
        return application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    @Provides
    fun provideVibrator(application: Application): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = application
                .getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
}
