//package io.nearpay.softpos.core.utils.enums
//
//import io.nearpay.terminalsdk.data.dto.MessageError
//import io.nearpay.softpos.core.data.remote.proxy.SocketMessage
//
//
//sealed class ProxyState {
//    object None : ProxyState()
//
//    data class NotConnected(val proxyType: ProxyType) : ProxyState()
//
//    object Pairing : ProxyState()
//
//    object Ponged : ProxyState()
//
//    object NotPonged : ProxyState()
//
//    data class Update(
//        val info: SocketMessage.Event.CashierUpdate
//    ) : ProxyState()
//
//    data class  Paired(
//        val info: SocketMessage.Event.ConnectionSuccess?
//    ) : ProxyState()
//
//    data class Error(
//        val error: MessageError
//    ) : ProxyState()
//
//    object Clear : ProxyState()
//}