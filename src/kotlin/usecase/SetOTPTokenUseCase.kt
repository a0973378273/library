package usecase;

import com.dexatek.bluetooth.DKBlueToothRepository
import com.dexatek.bluetooth.DataStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetOTPTokenUseCase(private val dkBluetoothRepository: DKBlueToothRepository) {
    operator fun invoke(
        blueToothAddress: String,
        deviceName: String,
        otpToken: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dkBluetoothRepository.setOTPToken(blueToothAddress,deviceName,otpToken).collect { result ->
                when (result) {
                    is DataStatus.Success -> onSuccess()
                    is DataStatus.Failed -> onError(result.exception)
                }
            }
        }
    }
}