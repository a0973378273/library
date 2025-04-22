package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.data.DKWiredNetworkData
import com.dexatek.bluetooth.tool.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadWiredNetworkUseCase(private val dkBluetoothRepository: DKBlueToothRepository) {
    operator fun invoke(
        bluetoothAddress: String,
        onSuccess: (DKWiredNetworkData) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.readWiredNetwork(bluetoothAddress).collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess(result.data)
                    is DataStatus.Failed -> onError(result.exception)
                }
            }
        }
    }
}