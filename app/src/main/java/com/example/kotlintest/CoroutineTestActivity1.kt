package com.example.kotlintest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.kotlintest.databinding.ActivityCoroutineTestBinding
import kotlinx.coroutines.*

/**
 * 协程使用；1。runBlocking:T；此时阻塞，但可以操作UI
 * 今后的代码全部使用 ViewBinding
 * 参考文章：https://www.jianshu.com/p/6e6835573a9c
 *
 * 2。调度器的使用
 * 协程可以在一个线程中挂起，在另外一个线程继续执行
 * 参考文章：https://www.jianshu.com/p/b88dda16e0e8
 *
 * 3。关键字：suspend、async
 * 参考文章：https://www.jianshu.com/p/6e6835573a9c
 */
public class CoroutineTestActivity1 : AppCompatActivity(), View.OnClickListener {

    var _binding: ActivityCoroutineTestBinding? = null
    val binding get() = _binding!!

    var tv_showinfo: TextView? = null

    val TAG: String = this::class.java.simpleName
    val testDispatcher: String = TAG + " testDispatcher()"
    val testUnconfinedDis: String = TAG + " testUnconfinedDis()"
    val testSwitchCorInThr: String = TAG + " testSwitchCorInThr()"
    val testSuspend: String = TAG + " testSuspend()"
    val testAsync: String = TAG + " testAsync()"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCoroutineTestBinding.inflate(getLayoutInflater())
        setContentView(binding.root)

        tv_showinfo = binding.tvCoreoutine
        tv_showinfo?.append("\nbingding 到了；这就开始～")

        binding.btnCoroutine1.setOnClickListener(this)
        binding.btnDispatch1.setOnClickListener(this)
        binding.btnDispatchConfined.setOnClickListener(this)
        binding.btnCorSwitchthread.setOnClickListener(this)
        binding.btnTestSuspend.setOnClickListener(this)
        binding.btnTestAsync.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnCoroutine1 -> {
                tv_showinfo?.append("\n--> 主线程ID：" + mainLooper.thread.id)
                test()
                tv_showinfo?.append("\n--> 协程执行结束")
            }
            //测试调度器
            binding.btnDispatch1 -> testDispatcher()

            //测试  Unconfined 调度器 和 confined 调度器
            binding.btnDispatchConfined -> testUnconfinedDis()

            //测试 线程间切换协程
            binding.btnCorSwitchthread -> testSwitchCorInThr()

            //测试 刮起协程体
            binding.btnTestSuspend -> testSuspend()

            //测试 关键字async
            binding.btnTestAsync->testAsync()
        }
    }

    /**
     * 测试 关键字 Async
     * async 跟 launch的用法基本一样，
     * 区别在于：async的返回值是 Deferred，将最后一个封装成了该对象。
     * async可以支持并发，此时一般都跟await一起使用。
     * async是不阻塞线程的,也就是说getResult1和getResult2是同时进行的，所以获取到result的时间是4s，而不是7s。
     */
    private fun testAsync() {
        GlobalScope.launch {
            val res1 = GlobalScope.async {
                getRes1()
            }
            val res2 = GlobalScope.async {
                getRes2()
            }
            val res =res1.await() +res2.await()
            println(testAsync +"res = " + res)
        }

    }

    private suspend fun getRes1():Int {
        delay(3000)
        return 1

    }

    private suspend fun getRes2() :Int{
        delay(4000)
        return 2
    }

    /**
     * 测试 挂起协程体
     * 协程体是一个用suspend关键字修饰的一个无参，无返回值的函数类型。
     * 被suspend修饰的函数称为挂起函数,与之对应的是关键字resume（恢复），
     * 注意：挂起函数只能在协程中和其他挂起函数中调用，不能在其他地方使用。
     * suspend函数会将整个协程挂起，而不仅仅是这个suspend函数，也就是说一个协程中有多个挂起函数时，它们是顺序执行的。
     * getToken方法将协程挂起，协程中其后面的代码永远不会执行，只有等到getToken挂起结束恢复后才会执行。
     * 同时协程挂起后不会阻塞其他线程的执行。
     * 另外，如果你要 delay 那么必须要使用 suspend
     */
    private fun testSuspend() {
        GlobalScope.launch {
            val token = getToken()
            val userInfo = getUserInfo(token)
            setUserInfo(userInfo)
        }
        repeat(8) {
            println(testSuspend + "主线程执行：" + it)
        }
    }

    private fun setUserInfo(userInfo: String) {
        println(testSuspend + userInfo)
    }

    private suspend fun getUserInfo(token: String): String {
        delay(2000)
        return token + " - userinfo"

    }

    private suspend fun getToken(): String {
        delay(2000)
        return "getToken 返回的 token"
    }


    /**
     * 测试 线程间切换协程
     * 通过newSingleThreadContext指定两个线程Ctx1，Ctx2。
     * 标准库中的 use 函数用来不需要时候释放newSingleThreadContext 所创建的线程。
     * runBlocking构建作用区域，运行协程，第一个打印，在ctx1线程中执行，
     * 通过 withContext 函数，更改协程的上下文，进行切换线程。
     * 切换到了ctx2线程。继续打印，因此当前协程运行在了ctx2线程。
     */
    private fun testSwitchCorInThr() {
        newSingleThreadContext("cxt1").use { cxt1 ->
            newSingleThreadContext("cxt2").use { cxt2 ->
                //在 cxt1 中执行协程
                runBlocking(cxt1) {
                    println(testSwitchCorInThr + "\t在 cxt1 输出：线程 = " + Thread.currentThread().name)
                    withContext(cxt2) {
                        println(testSwitchCorInThr + "\t切换到 cxt2 输出：线程 = " + Thread.currentThread().name)
                    }
                }

            }
        }
    }

    /**
     * 测试  Unconfined 调度器 和 confined 调度器
     * runBlocking 默认继承了上下文 的协程在主线程中（main）执行，
     * 而调度器是Unconfined的协程，在delay函数之后代码默认运行于delay函数所在的运行线程。 kotlinx.coroutines.DefaultExecutor。
     * 而confined调度器则并非如此，confined调度器的协程默认一直运行在runBlocking继承了上下文的线程（Main）
     */
    fun testUnconfinedDis() {
        runBlocking {
            launch {
                println(testUnconfinedDis + "\tconfined时：delay 之前运行在：" + Thread.currentThread().name)
                delay(2000)
                println(testUnconfinedDis + "\tconfined时：delay 之后运行在：" + Thread.currentThread().name)
            }

            launch(Dispatchers.Unconfined) {
                println(testUnconfinedDis + "\tUnconfined时：delay 之前运行在：" + Thread.currentThread().name + " 线程")
                delay(2000)
                println(testUnconfinedDis + "\tUnconfined时：delay 之后运行在：" + Thread.currentThread().name + " 线程")
            }
        }
    }

    /**
     * 测试 dispatcher 调度器
     * 协程调度器Dispatchers 可以将协程运行在指定的线程上，也可以将其分派到线程池中，或者让她无限制的运行
     * 不带参数和Unconfined的协程，运行在main主线程中。
     * Default则运行在 DefaultDispatcher-worker-1线程。
     * newSingleThreadContext参数，我们指定了一个新的线程NewThread。由于我们手动创建了新的线程。
     * 线程的资源非常稀缺，我们在不用的时候可以close掉。或者存在应用的顶级变量重复使用。
     */
    fun testDispatcher() {
        runBlocking {
            launch {
                println(testDispatcher + "\t无参数时：主线程：" + Thread.currentThread().name)
            }
            launch(Dispatchers.Default) {
                println(testDispatcher + "\t参数为：Dispatchers.Default时，运行在：" + Thread.currentThread().name + " 线程")
            }
            launch(Dispatchers.Unconfined) {
                println(testDispatcher + "\t参数为：Dispatchers.Unconfined 时，运行在：" + Thread.currentThread().name + " 线程")
            }
            launch(newSingleThreadContext("NewThread")) {
                println(testDispatcher + "\t参数为：newSingleThreadContext(\"NewThread\")时，运行在：" + Thread.currentThread().name + " 线程")
            }
        }

    }

    /**
     * 开启协程处理
     * runBlocking启动的协程任务会阻断当前线程，直到该协程执行结束。当协程执行结束之后，页面才会被显示出来。
     */
    private fun test() = runBlocking {
        repeat(8) {
            tv_showinfo?.append("\n--> 协程第 " + it + " 次；" + "线程ID ： " + Thread.currentThread().id)
            delay(1000)
        }

    }


}