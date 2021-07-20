package com.example.test

class DummyCaller(mockDummyCollaborator: DummyCollaborator) : DummyCallback {
    // 执行异步操作的代理类
    private  var dummyCollaborator: DummyCollaborator     // 执行结果
    private var result: List<String> = ArrayList<String>();

    init{
        this.dummyCollaborator = mockDummyCollaborator
    }
    fun doSomethingAsynchronously() {
        dummyCollaborator.doSomethingAsynchronously(this)
    }

    fun getResult(): List<String> {
        return this.result
    }

    override fun onSuccess(result: List<String?>?) {
        this.result = result as List<String>;
        System.out.println("On success");
    }

    override fun onFail(code: Int) {
        System.out.println("On Fail");
    }


}