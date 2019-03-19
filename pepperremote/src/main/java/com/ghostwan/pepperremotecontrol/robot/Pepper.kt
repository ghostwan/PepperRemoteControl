package com.ghostwan.pepperremotecontrol.robot

import com.aldebaran.qi.Future
import com.aldebaran.qi.Session
import com.aldebaran.qi.Tuple
import com.aldebaran.qi.UserTokenAuthenticator

class Pepper(
    private val endpoint: String,
    private val login: String="tablet",
    private val password: String) : Robot {

    private var session: Session = Session()

    override fun getRemoteEndpoint(): Future<String> {
        return getConnectedSession()
            .andThenCompose { it.service("ServiceDirectory") }
            .andThenCompose { it.call<Tuple>("service", "Actuation") }
            .andThenApply {
                if (it.size() >= 5) {
                    val list = it.get(4) as List<String>
                    return@andThenApply list.find { !it.contains("127.0.0.1") && !it.contains("198.18.0.1") }
                }
                throw NoEndpointFoundException()
            }
    }

    override fun getPublicToken(): String {
        return password
    }

    private fun getConnectedSession(): Future<Session> {
        return if (session.isConnected)
            Future.of(session)
        else {
            session = Session()
            session.setClientAuthenticator(UserTokenAuthenticator(login, password))
            session.connect(endpoint).andThenApply { session }
        }
    }

    class NoEndpointFoundException : Throwable()

}