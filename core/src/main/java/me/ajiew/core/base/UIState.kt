package me.ajiew.core.base

/**
 * 封装通用的 UI 状态
 */
sealed class UIState {
    object Loading : UIState()
    data class Success(val data: Any, val message: String = "") : UIState()
    data class Error(val errorData: Any? = null, val message: String) : UIState()
    data class Message(val message: String) : UIState()
}