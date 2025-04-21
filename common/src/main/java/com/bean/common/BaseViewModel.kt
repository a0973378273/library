package com.bean.common


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 定義UiState，用data class實作
 * @sample SampleUiState
 */
interface UiState

/**
 * 定義Action，用sealed interface實作
 * @sample SampleAction
 */
interface Action

/**
 * 定義Intent，用sealed interface實作
 * @sample SampleIntent
 */
interface Intent

/**
 * ViewModel 基底類別
 * @sample  SampleViewModel
 */
abstract class BaseViewModel<S : UiState, I : Intent, A : Action> : ViewModel() {
    /**
     * 初始UI狀態
     * @sample SampleFunctionViewModel.initialState
     */
    protected abstract val initialState: S
    private val _uiState by lazy { MutableStateFlow(initialState) }
    /**
     * 提供給 View 層來觀察資料狀態
     */
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _action = MutableSharedFlow<A>(0)
    /**
     * 提供給 View 層實作一次性動作
     */
    val action = _action.asSharedFlow()

    /**
     * 提供給 View 層來透過 Intent 來觸發行為
     */
    fun sendIntent(intent: I) {
        handleIntent(intent)
    }

    /**
     * 實作 Intent 的行為
     * @sample SampleFunctionViewModel.handleIntent
     */
    protected abstract fun handleIntent(intent: I)

    /**
     * 更新狀態
     * @param newState 新的狀態
     * @sample SampleFunctionViewModel.updateNewStateSample
     */
    protected fun updateState(newState: S) {
        _uiState.value = newState
    }

    /**
     * 使用 copy 更新狀態 (適用於 data class)
     * @param update 更新狀態的 lambda
     * @sample SampleFunctionViewModel.updateStateCopySample
     */
    protected fun updateState(update: S.() -> S) {
        _uiState.value = _uiState.value.update()
    }

    /**
     * 發送一次性動作
     * @sample SampleFunctionViewModel.sendAction
     */
    protected fun sendAction(action: A) {
        viewModelScope.launch {
            _action.emit(action)
        }
    }
}

data class SampleUiState(
    val loading : Boolean = false,
) : UiState

sealed interface SampleAction : Action {
    data class ShowToast(val message: String) : SampleAction
}

sealed interface SampleIntent : Intent {
    data object Intent : SampleIntent
    data class IntentParameter(val parameter: String) : SampleIntent
}

class SampleViewModel : BaseViewModel<SampleUiState, SampleIntent, SampleAction>() {
    override val initialState: SampleUiState
        get() = SampleUiState()
    override fun handleIntent(intent: SampleIntent) {
        when (intent) {
            is SampleIntent.Intent -> {}
            is SampleIntent.IntentParameter -> {}
        }
    }
}

class SampleFunctionViewModel : BaseViewModel<SampleUiState, SampleIntent, SampleAction>() {
    override val initialState: SampleUiState
        get() = SampleUiState()
    override fun handleIntent(intent: SampleIntent) {
        when (intent) {
            is SampleIntent.Intent -> {}
            is SampleIntent.IntentParameter -> {}
        }
    }
    private fun updateStateCopySample() {
        updateState { copy(loading = true) }
    }
    private fun updateNewStateSample() {
        updateState(SampleUiState(true))
    }
    private fun sendAction() {
        sendAction(SampleAction.ShowToast("Hello"))
    }
}