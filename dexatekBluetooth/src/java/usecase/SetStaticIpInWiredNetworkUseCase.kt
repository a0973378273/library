package usecase

import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetStaticIpInWiredNetworkUseCase(private val dkBluetoothRepository: DKBlueToothRepository) {

    fun interface JavaCallback {
        fun run()
    }

    fun interface JavaErrorCallback {
        fun run(throwable: Throwable)
    }

    fun execute(
        blueToothAddress: String,
        ipAddress: String,
        maskAddress: String,
        routerAddress: String,
        onSuccess: JavaCallback,
        onError: JavaErrorCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.setStaticIpInWiredNetwork(blueToothAddress, ipAddress, maskAddress, routerAddress).collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess.run()
                    is DataStatus.Failed -> onError.run(result.exception)
                }
            }
        }
    }
}