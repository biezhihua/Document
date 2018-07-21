# Jetpack

Jetpack是一系列库、工具、架构组成的，帮助开发人员快速方便的构建Anroid App。

[图片](https://github.com/googlesamples/android-sunflower/raw/master/screenshots/phone_plant_detail.png)

## 目标

1) 加速开发 
> 各个组件间相互独立，又可以彼此配合工作。使用kotlin特性能让生产效率更高。
2) 消除无用代码
> Android Jetpack管理各种枯燥的行为，例如后台任务、导航、生命周期管理，能让开发人员聚焦于App业务开发。
3) 构建高质量、健壮的app
> Android Jetpack组件会有更少的Crash和内存泄露以及向后兼容性。

## Android Jetpack 组件

### 基础组件

基础组件提供了系统的兼容性、Kotlin扩展、Mutidex分包以及自动化测试工具。

1) AppCompat
> 兼容旧版本的Anroid
2) Android KTX
> 帮助写出更简洁的Kotlin代码
> https://developer.android.com/kotlin/ktx
> https://github.com/googlesamples/android-sunflower
> https://www.youtube.com/watch?v=r_19VZ0xRO8&feature=youtu.be
2) Mulidex
> 提供多Dex支持(65535)
3) Test
> Android下的单元测试与UI测试框架

### 架构

架构组件帮助管理UI生命周期、数据持久化等。

1) Data Binding
> 声明性地将可观察数据绑定到UI元素。
2) Lifecycles
> 管理Activity和Fragment的生命周期
3) LiveData
> 当底层数据改变时通知View
4) Naviation
> 处理app内部的导航
5) Paging
> 逐步从数据园中加载需要的数据信息
6) Room
> 流畅的SQLite数据库访问
7) ViewModel
> 以可感知生命周期的方式来管理与UI相关的数据
8) WorkManager
> 管理Android后台任务

### 行为

行为组件帮助设计健壮、可测试、可维护的app

1) Download Manager
> 安排和管理大量的下载任务
2) Media & Playback
> 向后兼容的API，用于媒体播放和路由
> MediaCodec、MediaPlayer
3) Notifications
> 提供向后兼容的通知API
> NotificationCompat.Builder
5) Permissions
> 用于检查和请求应用权限的兼容性API
7) Sharing
> 提供一个分享操作
> ShareActionProvider
8) Slices
> 提供能在App之外展示(Google Search App 和 Googel Assistant）App数据的UI元素

### UI

1) Animation & Transitions
> 控件动画
2) Auto
>　汽车
3) Emoji
> 提供兼容的表情API
> EmojiCompat
4) Fragment
> 提供可组合UI的基本单元
5) Layout
> 布局
6) Palette
> 从调色板中提取有用信息
8) TV
> 帮助开发TV App
9)  Wear OS by Google
> 帮助开发手表 App


## WorkManager

WorkManager API可以轻松指定可延迟的异步任务以及何时运行它们。

这些API允许您创建任务并将其交给WorkManager立即运行或在适当的时间运行。

例如，应用可能需要不时从网络下载新资源,使用这些类，您可以设置任务，选择适当的环境来运行（例如“仅在设备正在充电和在线”时）,并将其交给WorkManager，以便在满足条件时运行。即使您的应用程序强制退出或设备重新启动，该任务仍可保证运行。

> 注意：WorkManager适用于需要保证系统即使应用程序退出也能运行它们的任务，例如将应用程序数据上传到服务器。如果应用程序进程消失，它不适用于可以安全终止进程内的后台工作;对于这种情况，我们建议使用ThreadPools

WorkManager根据设备API级别和应用程序状态等因素选择适当的方式来运行任务。如果WorkManager在应用程序运行时执行您的任务之一，WorkManager可以在您应用程序进程的新线程中运行您的任务。如果您的应用程序未运行，WorkManager会选择适当的方式来安排后台任务 - 根据设备API级别和包含的依赖项，WorkManager可能会使用JobScheduler，Firebase JobDispatcher或AlarmManager。您无需编写设备逻辑来确定设备具有哪些功能并选择适当的API;相反，您可以将您的任务交给WorkManager，让它选择最佳选项。

> 注意：如何引入WorkMananger库到你的工程中。https://developer.android.com/topic/libraries/architecture/adding-components#workmanager

**一些重要的API如下：**

1) Worker: 指定您需要执行的任务。 WorkManager API包含一个抽象的Worker类。您可以扩展此类并在此处执行工作。
2) WorkRequest: 代表一项单独的任务。 WorkRequest对象至少指定应该执行任务的Worker类。但是，您还可以向WorkRequest对象添加详细信息，指定诸如运行任务的环境之类的内容。每个WorkRequest都有一个自动生成的唯一ID;您可以使用ID执行取消排队任务或获取任务状态等操作。WorkRequest是一个抽象类;在您的代码中，您将使用直接子类之一，OneTimeWorkRequest或PeriodicWorkRequest。
3) WorkManager: 排队和管理工作请求。您将WorkRequest对象传递给WorkManager以将任务排入队列。 WorkManager以这样一种方式安排任务，即分散系统资源的负载，同时遵守您指定的约束。
4) WorkStatus:包含有关特定任务的信息。 WorkManager为每个WorkRequest对象提供LiveData。

**工作流程：**

```


    @RequiresApi(Build.VERSION_CODES.M)
    private fun normalTask() {
        // 创建任务约束
        val constraints = Constraints.Builder()
                .setRequiresDeviceIdle(true)
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build()

        // 创建一个工作任务
        val workRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .setConstraints(constraints)
                .build()

        // 将任务加入任务队列中
        WorkManager.getInstance()?.enqueue(workRequest)

        // 获取任务的状态
        WorkManager.getInstance()
                ?.getStatusById(workRequest.id)
                ?.observe(this, Observer { workStatus ->

                    // 检查工作任务状态
                    if (workStatus != null && workStatus.state.isFinished) {
                        Log.d("Work", "work state finish")
                    }
                })

        // 尝试去取消一个任务 （不确定一定能够成功，因为任务可能已经执行或者执行完毕）
        val workId: UUID = workRequest.id
        WorkManager.getInstance()
                //                ?.cancelAllWork()
                //                ?.cancelAllWorkByTag("")
                ?.cancelWorkById(workId)
    }


    class MyWorker : Worker() {

        override fun doWork(): Result {

            Log.d("Work", "do my work")

            return Result.SUCCESS
        }
    }
```

**高级功能：**

WorkManager API的核心功能可以创建简单，即发即弃的任务。除此之外，API还提供了先进的功能，可让设置更精细的请求。

1) 重复的任务：
```
    private fun recurringTask() {
        // 创建Builder
        val builder = PeriodicWorkRequest
                .Builder(MyWorker::class.java, 12, TimeUnit.SECONDS)

        // 创建任务
        val workRequest = builder.build()

        // 加入任务队列中
        WorkManager.getInstance()?.enqueue(workRequest)
    }
```
2) 链式任务: 应用可能需要按特定顺序运行多个任务。 WorkManager允许创建和排队指定多个任务的工作序列，以及它们应运行的顺序。
```
    private fun chainedTask() {

        val workA = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .build()
        
        val workA1 = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .build()
        
        val workA2 = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .build()
        
        val workA3 = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .build()

        val workB = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .build()

        val workC = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .build()   
        
        val workC1 = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .build()   
        
        val workC2 = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .build()

        WorkManager.getInstance()
                ?.beginWith(workA)
                // Note: WorkManager.beginWith() returns a
                // WorkContinuation object; the following calls are
                // to WorkContinuation methods
                ?.then(workB)    // FYI, then() returns a new WorkContinuation instance
                ?.then(workC)
                ?.enqueue()

        WorkManager.getInstance()
                // First, run all the A tasks (in parallel):
                .beginWith(workA1, workA2, workA3)
                // ...when all A tasks are finished, run the single B task:
                .then(workB)
                // ...then run the C tasks (in any order):
                .then(workC1, workC2)
                .enqueue()
    }
```

3) 唯一工作序列: 可以让你将一些任务追加、替换、丢弃到已经进入排队状态的序列中
```
    private fun uniqueWorkSequencesTask() {

        val workA = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .build()

        WorkManager.getInstance()
                ?.beginUniqueWork("work", ExistingWorkPolicy.APPEND, workA)
                ?.enqueue()

        WorkManager.getInstance()
                ?.beginUniqueWork("work", ExistingWorkPolicy.KEEP, workA)
                ?.enqueue()

        WorkManager.getInstance()
                ?.beginUniqueWork("work", ExistingWorkPolicy.REPLACE, workA)
                ?.enqueue()
    }
```
4) 标记任务：可以使用Tag对任务进行分组
```
    private fun taggedWorkTask() {
        val task = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .addTag("task")
                .build()
    }
```
5) 为任务参数和获取返回值：为了获得更大的灵活性，您可以将参数传递给任务并让任务返回结果。传递和返回的值是键值对。如果使用的是链式任务，还可以将结果传入下一个任务中。
   
```
fun Map<String, Int>.toWorkData(): Data {
    val builder = Data.Builder()
    forEach { s, i -> builder.putInt(s, i) }
    return builder.build()
}
```

    1. 要将参数传递给任务，请在创建WorkRequest对象之前调用WorkRequest.Builder.setInputData（）方法。

```
    private fun inputDataOutputData() {
        val myData: Data = mapOf(
                "KEY_X_ARG" to 42,
                "KEY_Y_ARG" to 421,
                "KEY_Z_ARG" to 8675309)
                .toWorkData()

        // ...then create and enqueue a OneTimeWorkRequest that uses those arguments
        val mathWork = OneTimeWorkRequest.Builder(MathWorker::class.java)
                .setInputData(myData)
                .build()
        WorkManager.getInstance()?.enqueue(mathWork)
    }
```

    2. 该方法采用Data对象，您使用Data.Builder创建。 Worker类可以通过调用Worker.getInputData（）来访问这些参数。
    3. 
```
// Define the parameter keys:
const val KEY_X_ARG = "X"
const val KEY_Y_ARG = "Y"
const val KEY_Z_ARG = "Z"

// ...and the result key:
const val KEY_RESULT = "result"

// Define the Worker class:
class MathWorker : Worker() {

    override fun doWork(): Result {
        val x = inputData.getInt(KEY_X_ARG, 0)
        val y = inputData.getInt(KEY_Y_ARG, 0)
        val z = inputData.getInt(KEY_Z_ARG, 0)

        // ...do the math...
        val result = myCrazyMathFunction(x, y, z);

        //...set the output, and we're done!
        val output: Data = mapOf(KEY_RESULT to result).toWorkData()

        outputData = output

        return Result.SUCCESS
    }

    private fun myCrazyMathFunction(x: Int, y: Int, z: Int): Int {
        return x + y + z
    }
}
```

    3. 要输出返回值，任务调用Worker.setOutputData（），它接受一个Data对象;您可以通过观察任务的LiveData <WorkStatus>来获取输出。

```
        WorkManager.getInstance()
                ?.getStatusById(mathWork.id)
                ?.observe(this, Observer { status ->
                    if (status != null && status.state.isFinished) {
                        val myResult = status.outputData.getInt(KEY_RESULT, -1)
                        // ... do something with the result ...
                    }
                })
```