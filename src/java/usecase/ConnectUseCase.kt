package usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.dexatek.bluetooth.DataStatus
import com.dexatek.bluetooth.DKBlueToothRepository

class ConnectUseCase(private val dkBluetoothRepository: DKBlueToothRepository) {
    fun interface JavaCallback {
        fun run()
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
            dkBluetoothRepository.connect(bluetoothAddress).collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess.run()
                    is DataStatus.Failed -> onError.run(result.exception)
                }
            }
        }
    }
}