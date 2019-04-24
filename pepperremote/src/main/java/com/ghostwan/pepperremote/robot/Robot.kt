package com.ghostwan.pepperremote.robot

import com.aldebaran.qi.Future

interface Robot {
    fun getRemoteEndpoint(): Future<String>
    fun getPublicToken(): String
    fun watchFocusLost(onFocusLost : () -> Unit)
}