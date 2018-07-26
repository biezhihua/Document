# Jetpack

Jetpack是一系列库、工具、架构组成的，帮助开发人员快速方便的构建Anroid App。

![这里写图片描述](https://img-blog.csdn.net/20180721183545677?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JpZXpoaWh1YQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

https://github.com/googlesamples/android-sunflower

https://github.com/biezhihua/Document

## 目标
------
1) 加速开发 
> 各个组件间相互独立，又可以彼此配合工作。使用kotlin特性能让生产效率更高。

2) 消除无用代码
> Android Jetpack管理各种枯燥的行为，例如后台任务、导航、生命周期管理，能让开发人员聚焦于App业务开发。

3) 构建高质量、健壮的app
> Android Jetpack组件会有更少的Crash和内存泄露以及向后兼容性。

## Android Jetpack 组件
------

### 基础组件
------
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
------

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
------


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
------

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

## Lifecycle
------

Lifecycle-Aware组件可以响应`Activity`、`Fragment`的生命周期变化。 可感知生命周期的组件，可以帮助写出更易维护、更轻量的代码。

一种常见的情况是在`Activity`和`Fragmen`t的生命周期方法中通知组件生命周期发生变化。但是，这种模式会导致代码组织不良以及错误的增加。通过使用可感知生命周期的组件，可以将依赖生命周期的代码从`Activity`和`Fragment`中移入到组件本身中。

```
class MyLocationListener {
    public MyLocationListener(Context context, Callback callback) {
        // ...
    }

    void start() {
        // 连接系统定位服务
    }

    void stop() {
        // 断开系统定位服务
    }
}


class MyActivity extends AppCompatActivity {

    private MyLocationListener myLocationListener;

    @Override
    public void onCreate(...) {
        myLocationListener = new MyLocationListener(this, (location) -> {
            // 更新UI
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        
        myLocationListener.start();
		
		// 管理其他需要响应生命周期的组件
    }

    @Override
    public void onStop() {
        super.onStop();
        
        myLocationListener.stop();
        
        // 管理其他需要响应生命周期的组件
    }
}
```

这个示例看起来很好，但在真正的开发中中，最终会有太多的代码调用来管理UI和其他组件以响应生命周期。管理多个组件会在生命周期方法中放置大量代码，例如`onStart()`和`onStop()`，这最终会导致难以维护。

此外，可能会产生一些生命周期异常。如果需要在`onStart()`执行长时间的配置检查操作，则可能会导致`onStop()`会在`onStart()`完成之前被调用。

`android.arch.lifecycle`提供了类和接口，可帮助我们优雅的方式解决这些问题。

### Lifecycle
----
**Lifecycle** 

[`Lifecycle`](https://developer.android.com/reference/android/arch/lifecycle/Lifecycle)是一个类，它包含有`Activity`或`Fragment`生命周期状态的信息，并允许其他对象观察此状态。

`Lifecycle`使用两个主要枚举来跟踪其关联组件的生命周期状态：

1) [`Event`](https://developer.android.com/reference/android/arch/lifecycle/Lifecycle.Event)： `Lifecycle`所跟踪组件`Activity`或`Fragment`回调的生命周期事件。
2) [`State`](https://developer.android.com/reference/android/arch/lifecycle/Lifecycle.State)： `Lifecycle`所跟踪组件`Activity`或`Fragment`的当前状态。

```
public class MyObserver implements LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectListener() {
        ...
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectListener() {
        ...
    }
}

myLifecycleOwner.getLifecycle().addObserver(new MyObserver());
```

![这里写图片描述](https://img-blog.csdn.net/20180724232217431?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JpZXpoaWh1YQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

为了更好的理解`State`和`Event`，可以将`State`视为图形的节点，将`Event`视为这些节点之间的边。

在上面的示例中，`myLifecycleOwner`对象实现了`LifecycleOwner`接口，将在下一节中进行说明。


### LifecycleOwner
-----

[`LifecycleOwner`](https://developer.android.com/reference/android/arch/lifecycle/LifecycleOwner)是一个单方法接口，表示该类具有生命周期。它仅有一个方法`getLifecycle()`，并且必须被继承者实现。

实现`LifecycleObserver`的组件与实现`LifecycleOwner`的组件可以无缝协作，因为所有者(`Activity`或`Fragment`)可以提供生命周期，而观察者可以监听生命周期回调。

### 可感知生命周期组件的最佳实践
-----

* 保持UI控制器（`Activity`和`Fragment`）尽可能精简。它们不应该试图获取自己的数据;相反，使用`ViewModel`执行此操作，并观察`LiveData`对象以将更改响应给`View`。
* 尝试编写数据驱动的UI，其中UI控制器负责在数据更改时更新`View`，或将用户操作通知给`ViewModel`。
* 将您的数据逻辑放在`ViewModel`类中。` ViewModel`应该充当UI控制器和应用程序其余部分之间的连接器。但要小心，`ViewModel`不负责获取数据（例如，从网络中获取）。相反，`ViewModel`应调用适当的组件来获取数据，然后将结果提供回UI控制器。
* 如果您的UI很复杂，请考虑创建一个`presenter`类来处理UI修改。这可能是一项艰巨的任务，但它可以使您的UI组件更容易测试。
* 避免在`ViewModel`中引用`View`或`Activity`相关的上下文。否则可能会引发内存泄露。

## [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)

简单地说，`LiveData`是一个数据持有类。它具有以下特点：

- 数据可以被观察者订阅；
- 能够感知组件（`Fragment`、`Activity`、`Service`）的生命周期；
- 只有在组件出于激活状态（`STARTED`、`RESUMED`）才会通知观察者有数据更新；

**`LiveData`具有如下优点：**

- **能够保证数据和UI统一 。**
这个和`LiveData`采用了观察者模式有关，`LiveData`是被观察者，当数据有变化时会通知观察者。这让我们可以在观察者中去更新UI，而不是每次数据变化时手动操纵更新UI。

- **没有内存泄漏。**
   这是因为`LiveData`能够感知到`Lifecycle`的生命周期，当组件处于`DESTROYED`状态时，观察者对象会被清除掉。

- **当`Activity`停止时不会引起崩溃**。
   这是因为组件处于非激活状态时，不会收到`LiveData`中数据变化的通知。

- **不需要手动处理生命周期状态**
   因为`LiveData`是可感知生命周期的组件，我们完全不需要手动告诉`LiveData`生命周期事件。

- **始终可保持最新数据**。
   当生命周期从不可见状态变为可见状态时，观察者可以收到最新的数据。

- **配置更改** 
如果由于配置更改（例如：旋转屏幕）而重新创建`Activity`或`Fragment`时，会立即接收最新的可用数据。

- **资源共享**
通过继承`LiveData`类，然后将该类定义成单例模式，并在该类中封装一些系统服务，当系统服务可用或者变化时后通知`LiveData`的观察者。

### 使用`LiveData`
----

按照如下步骤使用：
1. 创建`LiveData`对象，并让其持有一个具体类型的数据。通常在`ViewModel`中使用。
2. 创建`Observer`对象，并重写`onChange()`方法，当`LiveData`持有的数据发生改变时会回调此方法。`Observer`对象通常在UI控制器(`Activity`或`Fragment`)中使用。
3. 使用`LiveData`的`observer()`方法来订阅一个`Observer`对象，这样`LiveData`持有的数据发生变化时观察者就能够收到通知。通常在UI控制器中订阅观察者对象。

> 注意：如果使用`observeForever(Observer) `来订阅观察的话，该观察者会被认为始终处于激活状态，因此总是当数据变化时总是会收到通知。 此外，还可以通过`removeObserver(Observer)`方法来移除观察者。

当更新`LiveData`中的数据时，它会触发所有处于激活状态的观察者对象。

**创建`LiveData`对象**：

`LiveData`可以持有任何数据，通常`LiveData`对象被放置在`ViewModel`类中，然后通过`get`方法被UI控制器使用。
```
class NameViewModel : ViewModel() {
    
    private val name = MutableLiveData<String>()

    val nameLD: MutableLiveData<String> get() = name
}
```

**观察`LiveData`对象：**

一般来说，`onCreate()`方法都是观察`LiveData`正确的地方，原因如下：
1. 系统不会像对待`onResume()`一样，重复调用`onCreate()`方法。
2. 如果组件处于激活状态，保证其尽快展示数据。只要组件处于`STARTED`状态，那么它就会从`LiveData`中接收最近更新的数据。

**更新`LiveData`中的数据**：
`LiveData`中没有公用的方法可以更新存储数据，但是可以使用`MutableLiveData`暴露出来的`setValue(T)``postValue(T)`来更新数据。通常来说，`MutableLiveData`仅用于`ViewModel`内部，向外暴露时则提供`LiveData`给观察者对象。

通过调用`setValue(T)`方法，`LiveData`会通知所有处于激活状态的观察者对象，并调用其`onChange()`方法。
> `postValue(T)`用于在子线程中更新数据。

**无缝的配合Room使用**

~ 

### **继承`LiveData`类：**
----
当`LiveData`的观察者处于激活状态时`STARTED`或`RESUMED`，`LiveData`也可以被认为是一个观察者。它有如下方法：
```
@Override
 protected void onActive() {
 }

 @Override
 protected void onInactive() {
 }
```

- `onActive()`：当`LiveData`有一个处于激活状态的观察者时，该方法会被调用。
- `onInactive()`：当`LiveData`没有一个处于激活状态的观察者时，该方法会被调用。
- `setValue()`：用于更新持有的数据，通知所有观察者数据改变。

因为`LiveData`是生命周期可感知的，这意味着我们可以方便的将它在多个`Activity`和`Fragment`之间共享。（将其作为一个单例。）

### 转换`LiveData`
----

有时可能会有如下需求:
1. 能在`LiveData`中的值在其被分发给`Observer`时更改这个值。
2. 能基于`LiveData`中分发的值返回另一个`LiveData`。

`Transformations`可以帮助实现这些需求：

`Transformations.map()`：对存储在`LiveData`中的值应用一个函数，并将其继续分发给下游。
```
LiveData<User> userLiveData = ...;
LiveData<String> userName = Transformations.map(userLiveData, user -> {
    user.name + " " + user.lastName
});
```

`Transformations.switchMap()`:对存储在`LiveData`中的值应用一个函数，并值包装成一个`LiveData`继续分发给下游。
```
private LiveData<User> getUser(String id) {
  ...;
}

LiveData<String> userId = ...;
LiveData<User> user = Transformations.switchMap(userId, id -> getUser(id) );
```

> `Transformations`是懒加载的，只有`LiveData`被观察者订阅时，才会去计算结果。

假设我们有一个UI组件，接收一个地址，相应的返回一个邮政编码：

```
class MyViewModel extends ViewModel {
    private final PostalCodeRepository repository;
    public MyViewModel(PostalCodeRepository repository) {
       this.repository = repository;
    }

    private LiveData<String> getPostalCode(String address) {
       // DON'T DO THIS
       return repository.getPostCode(address);
    }
}
```

这种方式很不好，UI组件需要从对先前的`LiveData`对象解注册，并在每次调用`getPostalCode()`时再对新`LiveData`进行观察。此外，当UI组件在一些情况下被重建时，不会重用上次的数据，而是重新调用`getPostalCode()`方法，创建一个新的`LiveData`对象。

这里可以使用`Transformations.switchMap()`将地址作为输入，将其转换为一个`LiveData`。这样做的好处是，1）不需要频繁的解注册和再注册；2）`repository.getPostCode(address)`仅在`addressInput`发生改变时调用。

```
class MyViewModel extends ViewModel {

    private final PostalCodeRepository repository;
    
    private final MutableLiveData<String> addressInput = new MutableLiveData();
    
    public final LiveData<String> postalCode =
            Transformations.switchMap(addressInput, (address) -> {
                return repository.getPostCode(address);
             });

  public MyViewModel(PostalCodeRepository repository) {
      this.repository = repository
  }

  private void setInput(String address) {
      addressInput.setValue(address);
  }
}
```

> 这种机制允许较我们创建按需延迟计算的`LiveData`对象。

### 合并多个`LiveData`源

`MediatorLiveData`是`LiveData`的子类，可以合并多个`LiveData`源，其中任意一个`LiveData`持有的数据发生改变时都会发出通知。

## WorkManager
------

https://developer.android.com/topic/libraries/architecture/workmanager#chained
https://developer.android.com/reference/androidx/work/WorkManager

WorkManager API可以轻松指定可延迟的异步任务以及何时运行它们。

这些API允许您创建任务并将其交给WorkManager立即运行或在适当的时间运行。

例如，应用可能需要不时从网络下载新资源,使用这些类，您可以设置任务，选择适当的环境来运行（例如“仅在设备正在充电和在线”时）,并将其交给WorkManager，以便在满足条件时运行。即使您的应用程序强制退出或设备重新启动，该任务仍可保证运行。

> 注意：WorkManager适用于需要保证系统即使应用程序退出也能运行它们的任务，例如将应用程序数据上传到服务器。如果应用程序进程消失，它不适用于可以安全终止进程内的后台工作;对于这种情况，我们建议使用ThreadPools

WorkManager根据设备API级别和应用程序状态等因素选择适当的方式来运行任务。如果WorkManager在应用程序运行时执行您的任务之一，WorkManager可以在您应用程序进程的新线程中运行您的任务。如果您的应用程序未运行，WorkManager会选择适当的方式来安排后台任务 - 根据设备API级别和包含的依赖项，WorkManager可能会使用JobScheduler，Firebase JobDispatcher或AlarmManager。您无需编写设备逻辑来确定设备具有哪些功能并选择适当的API;相反，您可以将您的任务交给WorkManager，让它选择最佳选项。

> 注意：如何引入WorkMananger库到你的工程中。https://developer.android.com/topic/libraries/architecture/adding-components#workmanager

**一些重要的API如下：**

1) Worker: 指定您需要执行的任务。 
> WorkManager API包含一个抽象的Worker类。可以扩展此类并在此处执行工作。

2) WorkRequest: 代表一项单独的任务。
>  WorkRequest对象至少指定应该执行任务的Worker类。可以向WorkRequest对象添加详细信息，指定诸如运行任务的环境之类的内容。每个WorkRequest都有一个自动生成的唯一ID;可以使用ID执行取消排队任务或获取任务状态等操作。WorkRequest是一个抽象类;在代码中，将使用直接子类之一，OneTimeWorkRequest或PeriodicWorkRequest。

3) WorkManager: 排队和管理工作请求。
> 将WorkRequest对象传递给WorkManager以将任务排入队列。 WorkManager以这样一种方式安排任务，即分散系统资源的负载，同时遵守指定的约束。

4) WorkStatus:包含有关特定任务的信息。 
> WorkManager为每个WorkRequest对象提供LiveData。

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

1) 重复任务：
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

2) 链式任务: 
> 应用可能需要按特定顺序运行多个任务。 WorkManager允许创建和排队指定多个任务的工作序列，以及它们应运行的顺序。
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

3) 唯一工作序列: 
> 可以让你将一些任务追加、替换、丢弃已经进入排队状态的序列中
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

4) 标记任务：
> 可以使用Tag对任务进行分组
```
private fun taggedWorkTask() {
    val task = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .addTag("task")
            .build()
}
```
5) 为任务参数和获取返回值：
> 为了获得更大的灵活性，您可以将参数传递给任务并让任务返回结果。传递和返回的值是键值对。如果使用的是链式任务，还可以将结果传入下一个任务中。
   
```
fun Map<String, Int>.toWorkData(): Data {
    val builder = Data.Builder()
    forEach { s, i -> builder.putInt(s, i) }
    return builder.build()
}
```

 - 要将参数传递给任务，请在创建`WorkRequest`对象之前调用`WorkRequest.Builder.setInputData()`方法。

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

 - 该方法采用`Data`对象，使用`Data.Builder`创建。 Worker类可以通过调用`Worker.getInputData()`来访问这些参数。

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

 - 要输出返回值，任务调用`Worker.setOutputData()`，它接受一个`Data`对象;可以通过观察任务的`LiveData <WorkStatus>`来获取输出。

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

