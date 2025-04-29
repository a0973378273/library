package com.dexatek.bluetooth

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import usecase.StartDiscoveryUseCase

@RunWith(AndroidJUnit4::class)
class StartDiscoveryUseCaseTest : KoinTest {

    private val startDiscoveryUseCase: StartDiscoveryUseCase by inject()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        startKoin {
            androidContext(context)
            modules(dkBlueToothModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testStartDiscoveryReturnsData() = runBlocking {
        val results = mutableListOf<Any>()

        kotlinx.coroutines.withTimeout(10_000) {
            val job = launch {
                startDiscoveryUseCase(
                    onSuccess = { result ->
                        println("testStartDiscoveryReturnsData: ${result.name}")
                        results.add(result)
                    },
                    onError = { println("Discovery error: ${it.message}") }
                )
            }
            job.join()
        }

        assert(results.isNotEmpty()) { "Expected non-empty results after 10 seconds" }
    }

    fun test(){

    }
}