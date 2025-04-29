package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.data.DKBlueToothWIFIData
import com.dexatek.bluetooth.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadWifiListUseCase(private val dkBluetoothRepository: DKBlueToothRepository) {

    fun interface JavaCallback {
        fun run(dkBlueToothWIFIData: DKBlueToothWIFIData)
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
            dkBluetoothRepository.readWifiList(bluetoothAddress).collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess.run(result.data)
                    is DataStatus.Failed -> onError.run(result.exception)
                }
            }
        }
    }
}