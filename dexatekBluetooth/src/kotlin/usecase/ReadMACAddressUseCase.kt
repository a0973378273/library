package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadMACAddressUseCase(private val dkBluetoothRepository: DKBlueToothRepository) {
    operator fun invoke(
        bluetoothAddress: String,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.readMacAddress(bluetoothAddress).collect { result ->
                when (result) {
                    is DataStatus.Success -> {
                        val macAddress = result.data.joinToString(separator = ":") { "%02X".format(it) }
                        onSuccess(macAddress)
                    }
                    is DataStatus.Failed -> onError(result.exception)
                }
            }
        }
    }
}