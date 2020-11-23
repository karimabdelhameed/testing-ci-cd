package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueTest
import com.androiddevs.shoppinglisttestingyt.repositories.FakeShoppingRepository
import com.androiddevs.shoppinglisttestingyt.utils.Constants
import com.androiddevs.shoppinglisttestingyt.utils.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by karim on 21,November,2020
 */
@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ShoppingViewModel

    private val testDispatcher = TestCoroutineDispatcher()


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field return error`() {
        viewModel.insertShoppingItem("name", "", "3.0")

        val value = viewModel.insertShoppingItemStatusLiveData
            .getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name should return error`() {
        val tooLongName = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1){
                append('*')
            }
        }
        viewModel.insertShoppingItem(tooLongName, "5", "3.0")

        val value = viewModel.insertShoppingItemStatusLiveData
            .getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price should return error`() {
        val tooLongPrice = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH + 1){
                append(1)
            }
        }
        viewModel.insertShoppingItem("name", "5", tooLongPrice)

        val value = viewModel.insertShoppingItemStatusLiveData
            .getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with invalid amount should return error`() {

        viewModel.insertShoppingItem("name", "99999999999999999999999",
            "3.0")

        val value = viewModel.insertShoppingItemStatusLiveData
            .getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    @Test
    fun `insert shopping item with valid input should return success`() {

        viewModel.insertShoppingItem("name", "10", "3.0")

        val value = viewModel.insertShoppingItemStatusLiveData
            .getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }


}