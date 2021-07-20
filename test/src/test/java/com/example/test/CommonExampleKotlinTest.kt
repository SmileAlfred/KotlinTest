package com.example.test

import android.content.Context
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox
import java.io.File

/**
 * 文章
 * https://www.cnblogs.com/ljw-bim/p/9391770.html
 * https://feige.blog.csdn.net/article/details/113894056
 * https://zyc88.blog.csdn.net/article/details/88726299
 * https://www.shangmayuan.com/a/539483379a364de883430a77.html
 * https://blog.csdn.net/nanchengyu/article/details/112431258
 * https://blog.csdn.net/synpore/article/details/80352014
 * https://www.cnblogs.com/onblog/p/13044231.html
 */

//语句告诉JUnit用PowerMockRunner来执行测试。
@RunWith(PowerMockRunner::class)
//语句告诉PowerMock准备Employee类进行测试。适用于模拟final类或有final, private, static, native方法的类。
@PrepareForTest(
    CommonExampleKotlin::class,
    MUtils::class,
    CommonUtils::class,
    CommonDependency::class,
    EmployeeDao::class,
    MyCallBack::class
)
class CommonExampleKotlinTest {
    val res = "Return UUID"

    lateinit var commonExample: CommonExampleKotlin
    lateinit var mContext :Context

    @Before
    fun init() {
        commonExample = CommonExampleKotlin()
        mContext = PowerMockito.mock(Context::class.java)
    }

    //1) 普通Mock：Mock参数传递的对象
    @Test
    fun testCallArgumentInstance() {
        //1。创建一个mock对象
        val file = PowerMockito.mock(File::class.java)
        //2。指定mock对象具体的行为
        PowerMockito.`when`(file.exists()).thenReturn(false)
        //3。将mock对象作为参数传递给测试方法，执行测试方法。
        assert(commonExample.callArgumentInstance(file))
    }


    //2) Mock方法内部new出来的对象
    @Test
    fun testCallArgumentInstance2() {
        //代码实现RunWith
        MockitoAnnotations.initMocks(this);
        //1。创建一个mock对象
        val file = PowerMockito.mock(File::class.java)
        val commonExampleKotlin = CommonExampleKotlin()
        //2。指定当以参数为"fileName"创建File对象的时候，返回已经mock的File对象。
        PowerMockito.whenNew(File::class.java).withArguments("fileName").thenReturn(file)
        //3。指定mock对象具体的行为
        PowerMockito.`when`(file.exists()).thenReturn(true)
        //实际上此处该方法 并未成功创建文件，
        assert(commonExampleKotlin.callArgumentInstance("fileName"))

        val newFile = Mockito.mock(File::class.java)
        val existsNewF = newFile.exists()
        val exists = Mockito.verify(newFile).exists()
        println("file.exists? " + file.exists() + " ;  newFile.exists? " + existsNewF + " ; verify? " + exists)
    }

    //2.2)
    @Test
    fun testGetTotalEmployeeWithMock() {
        val employeeDao: EmployeeDao = PowerMockito.mock(EmployeeDao::class.java)
        try {
            //没得参数的够造！返回模拟的局部变量！
            PowerMockito.whenNew(EmployeeDao::class.java).withNoArguments()
                .thenReturn(employeeDao);
            PowerMockito.`when`(employeeDao.getTotal()).thenReturn(10);
            var total: Int = commonExample.getTotalEmployee()
            assert(10 == total);
        } catch (e: Exception) {
            fail("测试失败.");
        }
    }

    //3) Mock 普通对象的 final 方法
    @Test
    //final方法所在的类。
    @PrepareForTest(CommonDependency::class)
    fun testCallFinalMethod() {
        val mock: CommonDependency = PowerMockito.mock(CommonDependency::class.java)
        val commonExampleKotlin = CommonExampleKotlin()
        PowerMockito.`when`(mock.isAlive()).thenReturn(true)
        assert(commonExampleKotlin.callFinalMethod(mock))
    }

    //4) Mock 静态类的静态方法
    @Test
    fun testPrintUUID() {
        val commonExampleKotlin = CommonExampleKotlin()

        PowerMockito.mockStatic(MUtils::class.java)
        PowerMockito.`when`(MUtils.generateNewUUId()).thenReturn(res)
        print(commonExampleKotlin.printUUID())
        assert(res.equals(commonExampleKotlin.printUUID()))
    }

    //5) Mock 私有方法
    @Test
    fun testCallPrivateMethod() {
        val mock: CommonExampleKotlin = PowerMockito.mock(CommonExampleKotlin::class.java)
        PowerMockito.`when`(mock.callPrivateMethod()).thenCallRealMethod()
        PowerMockito.`when`<Boolean>(mock, "isExist").thenReturn(true)
        print(mock.callPrivateMethod())
        assertTrue(mock.callPrivateMethod())
    }

    //6) Mock系统类的静态和final方法
    @Test
    fun testcallSystemStaticMethod() {
        val commonExample = CommonExampleKotlin();
        PowerMockito.mockStatic(System::class.java);
        PowerMockito.`when`<String>(System.getProperty("aaa")).thenReturn("bbb");
        assertEquals("bbb", commonExample.callSystemStaticMethod("aaa"));
    }

    //7) Mock普通类的私有变量
    @Test
    fun testdoSomethingIfStateReady() {
        val sample = CommonExampleKotlin()
        Whitebox.setInternalState(sample, "mState", 1)
        assertTrue(sample.doSomethingIfStateReady())
    }

    //8) Mock 接口
    @Test
    fun testcallInterface() {
        val mock: MyCallBack = PowerMockito.mock(MyCallBack::class.java)
        PowerMockito.`when`(mock.stateEntered(0)).thenReturn(10)
        PowerMockito.`when`(mock.stateExited(1)).thenReturn(true)
        PowerMockito.`when`(mock.stateChanged(1, 2)).thenReturn("return")

        /**
         * org.mockito.exceptions.base.MockitoException:
         * Checked exception is invalid for this method!
         * Invalid: java.io.IOException
         */
        //PowerMockito.`when`(mock.stateChanged (2,3)).thenThrow(NullPointerException())
        //PowerMockito.`when`(mock.stateChanged (2,3)).thenThrow(NullPointerException())
    }

    //9) Mock Context;Mock 本就是 mock 到的空对象，如果使用 mock 的对象调 context 的方法；直接就报空指针；
    @Test
    fun testcallContext(){
        PowerMockito.`when`(mContext.getString(R.string.app_name)).thenReturn(res)
        assert(res.equals( commonExample.callContext()))
    }

}