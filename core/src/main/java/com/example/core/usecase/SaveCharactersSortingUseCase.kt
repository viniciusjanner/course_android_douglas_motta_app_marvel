package com.example.core.usecase

import com.example.core.data.mapper.SortingMapper
import com.example.core.data.repository.StorageRepository
import com.example.core.usecase.base.CoroutinesDispatchers
import com.example.core.usecase.base.ResultStatus
import com.example.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SaveCharactersSortingUseCase {
    //
    // operator : nos permite suprimir o .invoke na chamada do método.
    //
    operator fun invoke(params: Params): Flow<ResultStatus<Unit>>

    data class Params(val sortingPair: Pair<String, String>)
}

class SaveCharactersSortingUseCaseImpl @Inject constructor(
    private val storageRepository: StorageRepository,
    private val sortingMapper: SortingMapper,
    private val coroutinesDispatchers: CoroutinesDispatchers,
) : UseCase<SaveCharactersSortingUseCase.Params, Unit>(),
    SaveCharactersSortingUseCase {

    override suspend fun doWork(params: SaveCharactersSortingUseCase.Params): ResultStatus<Unit> {
        return withContext(coroutinesDispatchers.io()) {
            storageRepository.saveSorting(
                sortingMapper.mapFromPair(params.sortingPair),
            )
            ResultStatus.Success(Unit)
        }
    }
}
