package rachman.forniandi.workmanagerapp

import android.content.Context
import android.util.Log
import androidx.test.espresso.internal.inject.InstrumentationContext
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.junit.Before

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
}