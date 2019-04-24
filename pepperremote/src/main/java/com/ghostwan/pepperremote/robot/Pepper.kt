package com.ghostwan.pepperremote.robot

import android.util.Log
import com.aldebaran.qi.*
import timber.log.Timber

class Pepper(
    private val endpoint: String,
    private val password: String,
    private val login: String="tablet") : Robot {


    private var session: Session = Session()
    private var signal: QiSignalConnection? = null

    override fun getRemoteEndpoint(): Future<String> {
        return getConnectedSession()
            .andThenCompose { it.service("ServiceDirectory") }
            .andThenCompose { it.call<Tuple>("service", "Actuation") }
            .thenCompose {
                if(it.hasError())
                    Timber.e(it.error)
                return@thenCompose it
            }
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

    override fun watchFocusLost(onFocusLost: () -> Unit) {
        getConnectedSession()
            .andThenCompose { it.service("Focus") }
            .andThenCompose { it.call(AnyObject::class.java, "take", "password") }
            .andThenConsume { signal = it.connect("released") {
                onFocusLost()
            }}.thenConsume {
                when {
                    it.isCancelled -> Timber.w("Future cancel!")
                    it.hasError()-> Timber.w("Future as error :")
                }
            }
    }

    class NoEndpointFoundException : Throwable()

}