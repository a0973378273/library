package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.data.DKBlueToothWIFIData
import com.dexatek.bluetooth.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetWifiUseCase(private val dkBluetoothRepository: DKBlueToothRepository) {
    fun interface JavaCallback {
        fun run()
    }

    fun interface JavaErrorCallback {
        fun run(throwable: Throwable)
    }

    fun execute(
        dkBlueToothWIFIData: DKBlueToothWIFIData,
        password: String,
        onSuccess: JavaCallback,
        onError: JavaErrorCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.setWifi(dkBlueToothWIFIData, password).collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess.run()
                    is DataStatus.Failed -> onError.run(result.exception)
                }
            }
        }
    }
}