package usecase

import com.bean.bluetooth.BlueToothRepository
import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.tool.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import usecase.StartDiscoveryUseCase.JavaCallback
import usecase.StartDiscoveryUseCase.JavaErrorCallback

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