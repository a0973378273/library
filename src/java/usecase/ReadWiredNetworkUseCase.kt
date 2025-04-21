package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.data.DKWiredNetworkData
import com.dexatek.bluetooth.tool.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadWiredNetworkUseCase(private val dkBluetoothRepository: DKBlueToothRepository) {

    fun interface JavaCallback {
        fun run(dkWiredNetworkData: DKWiredNetworkData)
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
            dkBluetoothRepository.readWiredNetwork(bluetoothAddress).collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess.run(result.data)
                    is DataStatus.Failed -> onError.run(result.exception)
                }
            }
        }
    }
}