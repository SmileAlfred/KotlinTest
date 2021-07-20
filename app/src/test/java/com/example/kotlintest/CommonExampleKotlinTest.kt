package com.example.kotlintest

import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

//语句告诉JUnit用PowerMockRunner来执行测试。
@RunWith(PowerMockRunner::class)
//语句告诉PowerMock准备Employee类进行测试。适用于模拟final类或有final, private, static, native方法的类。
@PrepareForTest(MUtils::class, CommonExampleKotlin::class)
class CommonExampleKotlinTest {

    //4) ??????? Mock普通类的静态方法
    @Test
    fun testprintUUID() {
        val commonExampleKotlin = CommonExampleKotlin()
        PowerMockito.mockStatic(MUtils::class.java)
        PowerMockito.`when`(MUtils.generateNewUUId()).thenReturn("Return UUID")
        print(commonExampleKotlin.printUUID())
        assert(!commonExampleKotlin.printUUID().equals("Return UUID"))
    }
}