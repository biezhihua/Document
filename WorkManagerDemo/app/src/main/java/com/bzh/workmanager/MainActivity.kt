package com.bzh.workmanager

import android.arch.lifecycle.Observer
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

fun Map<String, Int>.toWorkData(): Data {
    val builder = Data.Builder()
    forEach { s, i -> builder.putInt(s, i) }
    return builder.build()
}

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // normalTask()
        // recurringTask()
        chainedTask()
    }

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


        WorkManager.getInstance()
                ?.getStatusById(mathWork.id)
                ?.observe(this, Observer { status ->
                    if (status != null && status.state.isFinished) {
                        val myResult = status.outputData.getInt(KEY_RESULT, -1)
                        // ... do something with the result ...
                    }
                })
    }

    private fun taggedWorkTask() {
        val task = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .addTag("task")
                .build()

        WorkManager.getInstance()
                ?.cancelAllWorkByTag("")
    }

    private fun uniqueWorkSequencesTask() {

        val workA = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .build()

        // Append your new sequence to the existing one, running the new sequence's first task after the existing sequence's last task finishes
        WorkManager.getInstance()
                ?.beginUniqueWork("work", ExistingWorkPolicy.APPEND, workA)
                ?.enqueue()

        // Keep the existing sequence and ignore your new request
        WorkManager.getInstance()
                ?.beginUniqueWork("work", ExistingWorkPolicy.KEEP, workA)
                ?.enqueue()

        // Cancel the existing sequence and replace it with the new one
        WorkManager.getInstance()
                ?.beginUniqueWork("work", ExistingWorkPolicy.REPLACE, workA)
                ?.enqueue()
    }

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
                ?.beginWith(workA1, workA2, workA3)
                // ...when all A tasks are finished, run the single B task:
                ?.then(workB)
                // ...then run the C tasks (in any order):
                ?.then(workC1, workC2)
                ?.enqueue()
    }

    private fun recurringTask() {
        // 创建Builder
        val builder = PeriodicWorkRequest
                .Builder(MyWorker::class.java, 12, TimeUnit.SECONDS)

        // 创建任务
        val workRequest = builder.build()

        // 加入任务队列中
        WorkManager.getInstance()?.enqueue(workRequest)
    }

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
}

class MyWorker : Worker() {

    override fun doWork(): Result {

        Log.d("Work", "do my work")

        return Result.SUCCESS
    }
}

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