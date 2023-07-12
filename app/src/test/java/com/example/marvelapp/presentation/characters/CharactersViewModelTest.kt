package com.example.marvelapp.presentation.characters

import androidx.paging.PagingData
import com.example.core.domain.model.Character
import com.example.core.usecase.GetCharactersUseCase
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class CharactersViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var getCharactersUseCase: GetCharactersUseCase

    private lateinit var charactersViewModel: CharactersViewModel

    private val charactersFactory = CharacterFactory()

    private val pagingDataCharacters = PagingData.from(
        listOf(
            charactersFactory.create(CharacterFactory.Hero.ThreeDMan),
            charactersFactory.create(CharacterFactory.Hero.ABomb),
        ),
    )

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        charactersViewModel = CharactersViewModel(getCharactersUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should validate the paging data object values when calling charactersPagingData`() =
        //
        // deve validar os valores do objeto de dados de paginação ao chamar os personagensPagingData
        //
        runTest {
            whenever(
                getCharactersUseCase.invoke(any()),
            ).thenReturn(
                flowOf(
                    pagingDataCharacters,
                ),
            )
            val result: Flow<PagingData<Character>> = charactersViewModel.charactersPagingData("")

            assertEquals(1, result.count())
        }

    @ExperimentalCoroutinesApi
    @Test(expected = RuntimeException::class)
    fun `should throw an exception when the calling to the use case returns an exception`() =
        //
        // deve lançar uma exceção quando a chamada para o caso de uso retornar uma exceção
        //
        runTest {
            whenever(
                getCharactersUseCase.invoke(any()),
            ).thenThrow(
                RuntimeException(),
            )
            val result: Flow<PagingData<Character>> = charactersViewModel.charactersPagingData("")
        }
}