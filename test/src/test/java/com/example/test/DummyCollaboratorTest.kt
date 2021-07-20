package com.example.test

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

/**
 * 测试 powermock 异步
 */
//语句告诉JUnit用PowerMockRunner来执行测试。
@RunWith(PowerMockRunner::class)
//语句告诉PowerMock准备Employee类进行测试。适用于模拟final类或有final, private, static, native方法的类。
@PrepareForTest(DummyCallback::class, DummyCaller::class, DummyCollaborator::class)
class DummyCollaboratorTest {
    // 要测试的类型
    private lateinit var dummyCaller: DummyCaller

    private lateinit var mockDummyCollaborator: DummyCollaborator;
    private lateinit var dummyCallbackArgumentCaptor: ArgumentCaptor<DummyCallback>

    @Before
    fun setUp() {
        mockDummyCollaborator = PowerMockito.mock(DummyCollaborator::class.java)
        dummyCallbackArgumentCaptor = ArgumentCaptor.forClass(DummyCallback::class.java)
        MockitoAnnotations.initMocks(this);
        dummyCaller = DummyCaller(mockDummyCollaborator);
    }

    @Test
    fun doSomethingAsynchronously() {
        // 调用要被测试发函数
        dummyCaller.doSomethingAsynchronously();
        val results: List<String> = Arrays.asList("One", "Two", "Three");
        // Let's call the callback. ArgumentCaptor.capture() works like a matcher.
        verify(mockDummyCollaborator).doSomethingAsynchronously(
            dummyCallbackArgumentCaptor.capture()
        )
        // 在执行回调之前验证结果
        assert(dummyCaller.getResult().isEmpty())
        // 调用回调的onSuccess函数
        dummyCallbackArgumentCaptor.getValue().onSuccess(results)
        // 再次验证结果
        assert(results.equals(dummyCaller.getResult()));
    }


    /*@Test
    fun testDoSomethingAsynchronouslyUsingDoAnswer() {
        val results:List<String>  = Arrays.asList("One", "Two", "Three");
        // 为callback执行一个同步anwser
         doAnswer( Answer() {
             @Override
             fun  answer(InvocationOnMock invocation) :Objectthrows Throwable {
             ((DummyCallback)invocation.getArguments()[0]).onSuccess(results);
             return null;            }        }).when(mockDummyCollaborator).doSomethingAsynchronously(
             any(DummyCallback.class));
        // 调用被测试的函数
        dummyCaller.doSomethingAsynchronously();
        // 验证状态与结果
          verify(mockDummyCollaborator, times(1))
              .doSomethingAsynchronously(  any(DummyCallback.class));
     assertThat(dummyCaller.getResult(), is(equalTo(results)));    }*/
}
