package com.example.clock4me.data.workManager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.clock4me.data.manager.WorkRequestManager
import com.example.clock4me.data.repository.AlarmRepository
import com.example.clock4me.data.workManager.worker.AlarmWorker
import com.example.clock4me.util.helper.AlarmNotificationHelper
import com.example.clock4me.util.helper.MediaPlayerHelper
import javax.inject.Inject

class AlarmWorkerFactory @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmNotificationHelper: AlarmNotificationHelper,
    private val mediaPlayerHelper: MediaPlayerHelper,
    private val workRequestManager: WorkRequestManager,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return AlarmWorker(
            alarmRepository,
            alarmNotificationHelper,
            mediaPlayerHelper,
            workRequestManager,
            appContext,
            params
        )
    }
}
