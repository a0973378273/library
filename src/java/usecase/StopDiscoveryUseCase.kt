package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StopDiscoveryUseCase(
    private val dkBluetoothRepository: DKBlueToothRepository
) {
    fun execute(
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.stopDiscovery()
        }
    }
}