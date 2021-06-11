package com.example.kotlintest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.core.app.AppLaunchChecker
import com.example.kotlintest.databinding.ActivityCoroutineTest2Binding
import kotlinx.coroutines.*
import java.lang.reflect.Method
import kotlin.concurrent.thread

/**
 * 协程使用；2.launch:Job；此时 不 阻塞，但 不 可以操作UI
 * 今后的代码全部使用 ViewBinding
 * 参考文章：https://www.jianshu.com/p/6e6835573a9c
 */
class CoroutineTestActivity2 : AppCompatActivity(), View.OnClickListener,
    CoroutineScope by CoroutineScope(Dispatchers.Default) {
    var _binding: ActivityCoroutineTest2Binding? = null
    val binding get() = _binding!!

    val TAG: String = this::class.java.simpleName
    val getJob: String = TAG + " getJob()\t"
    val testChildCor: String = TAG + " testChildCor()\t"
    val renameCor: String = TAG + " renameCor()\t"
    val testCorScope: String = TAG + " testCorScope()\t"
    val testThreadLocalData: String = TAG + " testThreadLocalData()\t"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCoroutineTest2Binding.inflate(getLayoutInflater())
        setContentView(binding.root)

        binding.tvCoroutlinJob.append("\nbinding上了～")

        binding.btnCoroutineJob.setOnClickListener(this)
        binding.btnGetJob.setOnClickListener(this)
        binding.btnChildCor.setOnClickListener(this)
        binding.btnRename.setOnClickListener(this)
        binding.btnCorScope.setOnClickListener(this)
        binding.btnThreadlocalsData.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            //测试 协程的使用，Job
            binding.btnCoroutineJob -> testJob()
            //测试 获取 Job
            binding.btnGetJob -> getJob()
            //测试 子协程
            binding.btnChildCor -> testChildCor()
            //测试 协程重命名
            binding.btnRename -> renameCor()
            //测试 协程作用域
            binding.btnCorScope -> testCorScope()
            //测试 线程局部数据
            binding.btnThreadlocalsData -> testThreadLocalData()
        }

    }

    /**
     * 线程局部数据（Thread-local data）
     * 有时候需要将线程的一些局部数据传递到协程中，但是协程没有绑定任何线程，
     * 所以可以通过ThreadLocal来实现，ThreadLocal的扩展函数asContextElement 可以解决这个问题
     */
    val threadLocal = ThreadLocal<String>()
    private fun testThreadLocalData() {
        threadLocal.set("this is new data")
        runBlocking {
            val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
                println(testThreadLocalData + "start data is -> " + threadLocal.get())
                yield()
                println(testThreadLocalData + "stop data is -> " + threadLocal.get())
            }
            job.join()
            println(testThreadLocalData + "and data is -> " + threadLocal.get())
        }
    }


    /**
     * 测试 协程作用域
     * 假如我们在一个Activity中启动了N个协程执行任务，并在Actvity销毁的时候，结束掉协程。
     * 这个时候我们能想到通过Actvity的声明周期和协程绑定显示的cancel协程，我们也可以通过实现CoroutineScope 接口。
     * 使用方法：
     * 1。实现接口：class MyActivity:CoroutineScope by CoroutineScope(Dispatchers.Default) {
     * 2。destroy() 方法中调用 cancel()ß
     * 注意：如果不如此，那可不就内存泄漏了。
     */
    private fun testCorScope() {
        repeat(10) { i ->
            launch {
                delay((i + 1) * 2000L)
                println(testCorScope + "这是第 " + i + " 次执行")
            }
        }
        finish()//模拟手动退出
    }

    /**
     * 测试 协程重命名
     * 使用CoroutineName为协程命名
     * TODO:实际上并没有看出来是怎么调用；
     * TODO:async、await 都没看懂是啥
     */
    private fun renameCor() {
        runBlocking(CoroutineName("main main main")) {
            val result = async(CoroutineName("别名哦～")) {
                println(renameCor + "test")
                5
            }
            println(renameCor + result.await())
        }
    }

    /**
     * 测试 子协程（Children of a coroutine）
     * 当一个协程在另外一个协程中启动的时候，默认继承了上面的协程的上下文，上面的协程称之为父协程。
     * 父协程取消执行的时候，子线程也会跟着取消。
     * 但是如果子线程用的作用区域是GlobalScope，全局作用区域，则不会影响
     *
     * 但：父协程会等待所有子协程完成任务
     */
    private fun testChildCor() {
        runBlocking {
            var job = launch {
                println(testChildCor + " 父协程～")
                launch {
                    println(testChildCor + " 子协程 ～")
                    delay(2000)
                    println(testChildCor + " 子协程 结束调用～")
                }

                GlobalScope.launch {
                    println(testChildCor + " global 子协程 ～")
                    delay(2000)
                    println(testChildCor + " global 子协程 1 结束调用 ～")
                }
            }
            delay(500)
            job.cancel()
            //保持 JVM 活跃
            delay(
                2000
            )

        }

    }

    /**
     * 测试 获取Job
     */
    private fun getJob() {
        runBlocking {
            launch {
                println(getJob + "Job is " + coroutineContext[Job])
            }
        }

    }

    /**
     * 测试 协程的使用，Job
     * 通过coroutineContext[Job]获取
     */
    private fun testJob() {
        //主线程：2
        binding.tvCoroutlinJob.append("\n-->  主线程：" + mainLooper.thread.id)
        println("\n-->  主线程：" + mainLooper.thread.id)

        val job = GlobalScope.launch {
            delay(2000)
            //不可切换至主线程；右侧也不可行：Looper.prepare()//       Looper.loop()
            //协程（Job）执行结束～线程ID：1669
            println("-->  协程（Job）执行结束～线程ID：" + Thread.currentThread().id)
            //binding.tvCoroutlinJob.append("\n-->  协程（Job）执行结束～")
        }

        //Job中的方法
        val active = job.isActive
        val cancelled = job.isCancelled
        val completed = job.isCompleted
        //job.cancel()//调用了 cancel 之后并不会立刻结束，但终究还是会结束；
        println(
            "-->  activie ? " + active
                    + " ; canclled  ? " + cancelled
                    + " ; comokted ? " + completed
        )
        //jon.join()

        println("\n-->  主协程执行结束～")
        binding.tvCoroutlinJob.append("\n-->  主协程执行结束～")
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}