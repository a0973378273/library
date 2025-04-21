package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.tool.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetDynamicIpInWiredNetworkUseCase(private val dkBluetoothRepository: DKBlueToothRepository) {
    operator fun invoke(
        blueToothAddress: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.setDynamicIpInWiredNetwork(blueToothAddress).collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess()
                    is DataStatus.Failed -> onError(result.exception)
                }
            }
        }
    }
}