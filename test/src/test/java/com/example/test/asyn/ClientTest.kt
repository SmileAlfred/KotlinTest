package com.example.test.asyn

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.doAnswer
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * 异步回调方法-PowerMock系列之6:https://cloud.tencent.com/developer/article/1754824
 *
 */
//语句告诉JUnit用PowerMockRunner来执行测试。
@RunWith(PowerMockRunner::class)
//语句告诉PowerMock准备Employee类进行测试。适用于模拟final类或有final, private, static, native方法的类。
@PrepareForTest(Client::class, Connector::class, Listener::class, IListener::class)
class ClientTest {
    var data = "SUCCESS"

    private lateinit var connector: Connector

    @Before
    fun initMocks() {
        connector = PowerMockito.spy(Connector())
        //connector = PowerMockito.mock(Connector::class.java)
    }

    @Test
    fun TestClientLogin() {
        val client = Client(connector, "localhost")

        val answer = Answer() {
            //3.当底层（通信层）收到消息时，通过connector.onMessage(data)来进行处理，
            // 即通知IListener，进而将登陆状态设置为登陆成功。
            connector.onMessage(data)
        }

        //1.通过connector.login来发送消息。
        doAnswer(answer).`when`(connector).login("localhost")
        client.login()
        //2.监控client.getConnected（）的状态，检查是否登陆成功。
        Assert.assertTrue(client.getConnected())
    }
}
