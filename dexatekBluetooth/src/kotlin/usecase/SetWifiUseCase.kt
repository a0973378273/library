package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.data.DKBlueToothWIFIData
import com.dexatek.bluetooth.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetWifiUseCase(private val dkBluetoothRepository: DKBlueToothRepository) {
    operator fun invoke(
        dkBlueToothWIFIData: DKBlueToothWIFIData,
        password: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.setWifi(dkBlueToothWIFIData,password).collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess()
                    is DataStatus.Failed -> onError(result.exception)
                }
            }
        }
    }
}