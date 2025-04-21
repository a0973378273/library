package usecase

import com.bean.bluetooth.BlueToothRepository
import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.tool.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConnectUseCase(private val dkBluetoothRepository: DKBlueToothRepository) {
    operator fun invoke(
        bluetoothAddress: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.connect(bluetoothAddress).collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess()
                    is DataStatus.Failed -> onError(result.exception)
                }
            }
        }
    }
}