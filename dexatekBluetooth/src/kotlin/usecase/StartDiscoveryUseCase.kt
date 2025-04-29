package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.data.DKBlueToothData
import com.dexatek.bluetooth.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartDiscoveryUseCase(
    private val dkBluetoothRepository: DKBlueToothRepository
) {
    operator fun invoke(
        onSuccess: (DKBlueToothData) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.startDiscovery().collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess(result.data)
                    is DataStatus.Failed -> onError(result.exception)
                }
            }
        }
    }
}