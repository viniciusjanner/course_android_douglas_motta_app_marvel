package com.example.marvelapp.framework.di

import com.example.core.data.repository.CharactersRemoteDataSource
import com.example.core.data.repository.CharactersRepository
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.remote.RetrofitCharactersDataSource
import com.example.marvelapp.framework.repository.CharactersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun binCharacterRepository(repository: CharactersRepositoryImpl): CharactersRepository

    @Binds
    fun binRemote(dataSource: RetrofitCharactersDataSource): CharactersRemoteDataSource<DataWrapperResponse>
}