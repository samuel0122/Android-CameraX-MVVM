package es.ua.eps.camerax_mvvm.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.ua.eps.camerax_mvvm.data.dataPreferences.DataPreferencesDao
import es.ua.eps.camerax_mvvm.data.dataPreferences.DataStorePreferencesDao
import es.ua.eps.camerax_mvvm.data.mediaStorage.ImagesStorageDao
import es.ua.eps.camerax_mvvm.data.mediaStorage.MediaStorageDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDataPreferencesDao(
        dataStorePreferencesDao: DataStorePreferencesDao
    ): DataPreferencesDao = dataStorePreferencesDao

    @Provides
    @Singleton
    fun provideMediaStorageDao(
        imagesStorageDao: ImagesStorageDao
    ): MediaStorageDao = imagesStorageDao

}
