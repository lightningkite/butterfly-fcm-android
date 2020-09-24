package com.lightningkite.butterfly.fcm

import com.lightningkite.butterfly.SwiftMustBeClass
import com.lightningkite.butterfly.observables.ObservableStack
import com.lightningkite.butterfly.swiftMustBeClass

@SwiftMustBeClass
interface ForegroundNotificationHandler {
    fun handleNotificationInForeground(map: Map<String, String>): ForegroundNotificationHandlerResult {
        println("Received notification in foreground with $map")
        return ForegroundNotificationHandlerResult.SHOW_NOTIFICATION
    }
}

enum class ForegroundNotificationHandlerResult {
    SUPPRESS_NOTIFICATION, SHOW_NOTIFICATION, UNHANDLED
}
