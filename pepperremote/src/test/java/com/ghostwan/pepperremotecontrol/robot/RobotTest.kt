package com.ghostwan.pepperremotecontrol.robot

import com.aldebaran.qi.DynamicObjectBuilder
import com.aldebaran.qi.ServiceDirectory
import com.aldebaran.qi.Session
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test


class RobotTest {

    private lateinit var url: String
    private lateinit var pepper: Pepper
    private lateinit var session: Session

    @Before
    fun setup() {
        val serviceDirectory = ServiceDirectory()
        session = Session()
        url = serviceDirectory.listenUrl()
        val objectBuilder = DynamicObjectBuilder()
        session.connect(url).get()
        session.registerService("Actuation", objectBuilder.`object`())
        pepper = Pepper(url, "nao")
    }

    @Test
    fun `public token is nao`() {
        pepper.getPublicToken() shouldBeEqualTo  "nao"
    }

    @Test
    fun `remote end point is the computer one`() {
        val endpoint = pepper.getRemoteEndpoint().get()
        endpoint shouldBeEqualTo url
    }

    @After
    fun clean() {
        session.close()
    }
}

