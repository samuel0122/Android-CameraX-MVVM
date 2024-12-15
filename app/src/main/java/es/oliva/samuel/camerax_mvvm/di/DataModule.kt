package es.oliva.samuel.camerax_mvvm.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.oliva.samuel.camerax_mvvm.data.dataPreferences.DataPreferencesDao
import es.oliva.samuel.camerax_mvvm.data.dataPreferences.DataStorePreferencesDao
import es.oliva.samuel.camerax_mvvm.data.mediaStorage.MediaStorageDao
import es.oliva.samuel.camerax_mvvm.data.mediaStorage.PicturesStorageDao
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
        picturesStorageDao: PicturesStorageDao
    ): MediaStorageDao = picturesStorageDao

}
