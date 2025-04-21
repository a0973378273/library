package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DisconnectUseCase(
    private val dkBluetoothRepository: DKBlueToothRepository
) {
    fun execute(
        bluetoothAddress: String,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.disconnect(bluetoothAddress)
        }
    }
}