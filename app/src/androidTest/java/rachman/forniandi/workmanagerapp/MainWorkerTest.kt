package rachman.forniandi.workmanagerapp

import android.content.Context
import android.util.Log
import androidx.test.espresso.internal.inject.InstrumentationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.*
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.`is`
import rachman.forniandi.workmanagerapp.data.work.EmployeeCoroutineWork
import rachman.forniandi.workmanagerapp.data.work.RandomNumberWork
import java.util.concurrent.TimeUnit

class MainWorkerTest {

    private lateinit var context: Context
    private lateinit var configuration:Configuration

    @Before
    fun setup(){
        context = InstrumentationRegistry.getInstrumentation().targetContext
        configuration = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
                //user a synchronousExecutor to make it easy to write the test
            .setExecutor(SynchronousExecutor())
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, configuration)
    }

    @Test
    fun testRandomNumberWork(){
        //set input data
        val inputData = workDataOf("KEY_START" to 0,"KEY_COUNT" to 0)

        //result output data
        val resultOutputData = workDataOf("KEY_RESULT" to 10)

        //request
        val request = OneTimeWorkRequestBuilder<RandomNumberWork>()
            .setInputData(inputData)
            .build()

        //workManager
        val workManager = WorkManager.getInstance(context)
        //enqueue the work for result
        workManager.enqueue(request).result.get()
        //work info
        val info = workManager.getWorkInfoById(request.id).get()
        val outputData = info.outputData
        //assert for result succeeded
        assertThat(info.state, `is`(WorkInfo.State.SUCCEEDED))
        //assert for result succeeded
        //assertThat(info.state, `is`(WorkInfo.State.FAILED))
        assertThat(outputData, `is`(resultOutputData))

    }

    @Test
    fun testWithInitialDelay(){
        //set input data
        val inputData = workDataOf("KEY_START" to 0,"KEY_COUNT" to 0)

        //result output data
        val resultOutputData = workDataOf("KEY_RESULT" to 10)

        //request
        val request = OneTimeWorkRequestBuilder<RandomNumberWork>()
            .setInputData(inputData)
            .setInitialDelay(30,TimeUnit.MILLISECONDS)
            .build()

        //workManager
        val workManager = WorkManager.getInstance(context)
        //enqueue the work for result
        val testDriver =WorkManagerTestInitHelper.getTestDriver(context)

        //enqueue the work for result
        workManager.enqueue(request).result.get()

        //tell the work manager that initial delay are set
        testDriver?.setInitialDelayMet(request.id)
        //work info
        val workInfo = workManager.getWorkInfoById(request.id).get()
        val outputData = workInfo.outputData
        //assert for result succeeded
        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
        //assert for result succeeded
        //assertThat(info.state, `is`(WorkInfo.State.FAILED))
        assertThat(outputData, `is`(resultOutputData))

    }


    @Test
    fun testWithConstraints(){
        //set input data
        val inputData = workDataOf("KEY_START" to 0,"KEY_COUNT" to 0)

        //result output data
        val resultOutputData = workDataOf("KEY_RESULT" to 10)

        //constraints
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        //request
        val request = OneTimeWorkRequestBuilder<RandomNumberWork>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        //workManager
        val workManager = WorkManager.getInstance(context)
        //enqueue the work for result
        val testDriver =WorkManagerTestInitHelper.getTestDriver(context)

        //enqueue the work for result
        workManager.enqueue(request).result.get()

        //tell the work manager that initial constraint are set
        testDriver?.setAllConstraintsMet(request.id)
        //work info
        val workInfo = workManager.getWorkInfoById(request.id).get()
        val outputData = workInfo.outputData
        //assert for result succeeded
        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
        //assert for result succeeded
        //assertThat(info.state, `is`(WorkInfo.State.FAILED))
        assertThat(outputData, `is`(resultOutputData))

    }


    @Test
    fun testWithPeriodicWork(){
        //set input data
        val inputData = workDataOf("KEY_START" to 0,"KEY_COUNT" to 0)

        //result output data
        val resultOutputData = workDataOf("KEY_RESULT" to 10)

        //constraints
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        //request
        val request = PeriodicWorkRequestBuilder<RandomNumberWork>(15,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        //workManager
        val workManager = WorkManager.getInstance(context)
        //enqueue the work for result
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)

        //enqueue the work for result
        workManager.enqueue(request).result.get()

        //tell the work manager that periodic work are set
        testDriver?.setPeriodDelayMet(request.id)
        //work info
        val workInfo = workManager.getWorkInfoById(request.id).get()
        val outputData = workInfo.outputData
        //assert for result enqueued
        assertThat(workInfo.state, `is`(WorkInfo.State.ENQUEUED))
        //assert for result succeeded
        //assertThat(info.state, `is`(WorkInfo.State.FAILED))

    }

    @Test
    fun testWithCoroutinesWorker(){
        //set input data
        val inputData = workDataOf("KEY_START" to 0,"KEY_COUNT" to 0)

        //worker
        val worker = TestListenableWorkerBuilder<EmployeeCoroutineWork>(context,inputData)
            .build()

        runBlocking {
            val doWork = worker.startWork().get()

            //result outputData
            val resultOutputData = workDataOf("KEY_RESULT" to doWork.outputData.getLong("KEY_RESULT",0))

            //assert
            assertEquals(resultOutputData,doWork.outputData)

            assertThat(ListenableWorker.Result.success(resultOutputData), `is`(doWork))
        }


    }


}