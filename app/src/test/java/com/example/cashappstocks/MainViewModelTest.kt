package com.example.cashappstocks

import com.example.cashappstocks.network.data.Result
import com.example.cashappstocks.network.data.Stock
import com.example.cashappstocks.network.StocksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description


internal class MainViewModelTest {

    lateinit var sut: MainViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = MainDispatcherRule(dispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cancel()
    }

    @Test
    fun `Test Scenario - Service response is succesful`() = runBlocking {
        sut =
            MainViewModel(FakeRepository())
        var latestState: MainViewModel.UIState = MainViewModel.UIState()
        val job = launch(dispatcher) {
            sut.uiState.take(2).collect {
                latestState = it
            }
        }
        sut.getData()
        Assert.assertEquals("There should be exactly 2 Stocks", 2, latestState.stocks.size)
        job.cancel()
    }

    @Test
    fun `Test Scenario - Service response is error`() = runBlocking {
        sut =
            MainViewModel(FakeErrorRepository())
        var latestState: MainViewModel.UIState = MainViewModel.UIState()
        val job = launch(dispatcher) {
            sut.uiState.take(2).collect {
                latestState = it
            }
        }
        sut.getData()
        Assert.assertEquals("Should Be Error State", true, latestState.isError)
        job.cancel()
    }
}


class FakeRepository : StocksRepository {

    override suspend fun getStocks(): Result<List<Stock>> {
        val stock = Stock("ABC", "CANADA", "USD", "333", 3, 333L)

        return Result.Success(
            listOf<Stock>(
                stock.copy("TICKER1"), stock.copy(ticker = "TICKER2")
            )
        )
    }
}

class FakeErrorRepository : StocksRepository {

    override suspend fun getStocks(): Result<List<Stock>> =
        Result.Error(204,"")

}

@ExperimentalCoroutinesApi
class MainDispatcherRule(val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {

    override fun starting(description: Description?) = Dispatchers.setMain(dispatcher)

    override fun finished(description: Description?) = Dispatchers.resetMain()

}