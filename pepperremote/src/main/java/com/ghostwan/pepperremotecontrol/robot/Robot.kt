package com.ghostwan.pepperremotecontrol.robot

import com.aldebaran.qi.Future

interface Robot {
    fun getRemoteEndpoint(): Future<String>
    fun getPublicToken(): String
}