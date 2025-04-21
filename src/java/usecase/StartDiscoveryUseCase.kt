package usecase

import com.dexatek.bluetooth.data.DKBlueToothData
import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.tool.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartDiscoveryUseCase(
    private val dkBluetoothRepository: DKBlueToothRepository
) {
    fun interface JavaCallback {
        fun run(dkBlueToothData: DKBlueToothData)
    }
    fun interface JavaErrorCallback {
        fun run(throwable: Throwable)
    }
    fun execute(
        onSuccess: JavaCallback,
        onError: JavaErrorCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.startDiscovery().collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess.run(result.data)
                    is DataStatus.Failed -> onError.run(result.exception)
                }
            }
        }
    }
}