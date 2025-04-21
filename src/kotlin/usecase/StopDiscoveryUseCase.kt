package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StopDiscoveryUseCase(
    private val dkBluetoothRepository: DKBlueToothRepository
) {
    operator fun invoke(
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.stopDiscovery()
        }
    }
}