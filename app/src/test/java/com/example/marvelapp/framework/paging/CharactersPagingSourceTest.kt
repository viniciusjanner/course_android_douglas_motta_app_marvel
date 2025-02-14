package com.example.marvelapp.framework.paging

import androidx.paging.PagingSource
import com.example.core.data.repository.CharactersRemoteDataSource
import com.example.core.domain.model.Character
import com.example.marvelapp.factory.response.CharacterPagingFactory
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharactersPagingSourceTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var remoteDataSource: CharactersRemoteDataSource

    private val characterPagingFactory = CharacterPagingFactory()

    private val characterFactory = CharacterFactory()

    private lateinit var charactersPagingSource: CharactersPagingSource

    @Before
    fun setUp() {
        charactersPagingSource = CharactersPagingSource(remoteDataSource, "")
    }

    @Test
    fun `should return a success load result when load is called`() =
        //
        // deve retornar um resultado de load com sucesso quando load é chamado
        //
        runTest {
            // Arrange
            whenever(
                remoteDataSource.fetchCharacters(any()),
            ).thenReturn(
                characterPagingFactory.create(),
            )

            // Act
            //
            // PagingSource possui Append, Prepend e Refresh
            // Refresh: quando chamando pela primeira vez (offset = 0 primeira pagina).
            // Prepend: quando ocorre o scroll de cima pra baixo.
            // Append: quanod ocorre scroll infinito.
            //
            val result = charactersPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    null,
                    loadSize = 2,
                    false,
                ),
            )

            // Assert
            val expected = listOf(
                characterFactory.create(CharacterFactory.Hero.ThreeDMan),
                characterFactory.create(CharacterFactory.Hero.ABomb),
            )
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = expected,
                    prevKey = null,
                    nextKey = 20,
                ),
                result,
            )
        }

    @Test
    fun `should return a error load result when load is called`() =
        //
        // deve retornar um resultado de load de erro quando load é chamado
        //
        runTest {
            // Arrange
            val exception = RuntimeException()
            whenever(
                remoteDataSource.fetchCharacters(any()),
            ).thenThrow(
                exception,
            )

            // Act
            //
            // PagingSource possui Append, Prepend e Refresh
            // Refresh: quando chamando pela primeira vez (offset = 0 primeira pagina).
            // Prepend: quando ocorre o scroll de cima pra baixo.
            // Append: quanod ocorre scroll infinito.
            //
            val result = charactersPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 2,
                    placeholdersEnabled = false,
                ),
            )

            // Assert
            assertEquals(
                PagingSource.LoadResult.Error<Int, Character>(exception),
                result,
            )
        }
}
