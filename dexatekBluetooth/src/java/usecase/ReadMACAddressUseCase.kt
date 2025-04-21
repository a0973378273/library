package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.tool.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadMACAddressUseCase(private val dkBluetoothRepository: DKBlueToothRepository) {

    fun interface JavaCallback {
        fun run(macAddress: String)
    }

    fun interface JavaErrorCallback {
        fun run(throwable: Throwable)
    }

    fun execute(
        bluetoothAddress: String,
        onSuccess: JavaCallback,
        onError: JavaErrorCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.readMacAddress(bluetoothAddress).collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess.run(result.data.toString())
                    is DataStatus.Failed -> onError.run(result.exception)
                }
            }
        }
    }
}