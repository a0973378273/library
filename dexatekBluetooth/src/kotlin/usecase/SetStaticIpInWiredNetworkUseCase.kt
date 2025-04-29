package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetStaticIpInWiredNetworkUseCase(private val dkBluetoothRepository: DKBlueToothRepository) {
    operator fun invoke(
        blueToothAddress: String,
        ipAddress: String,
        maskAddress: String,
        routerAddress: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.setStaticIpInWiredNetwork(blueToothAddress,ipAddress,maskAddress,routerAddress).collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess()
                    is DataStatus.Failed -> onError(result.exception)
                }
            }
        }
    }
}