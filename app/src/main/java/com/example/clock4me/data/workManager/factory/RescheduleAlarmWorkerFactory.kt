package com.example.clock4me.data.workManager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.clock4me.data.manager.ScheduleAlarmManager
import com.example.clock4me.data.manager.WorkRequestManager
import com.example.clock4me.data.repository.AlarmRepository
import com.example.clock4me.data.workManager.worker.RescheduleAlarmWorker
import javax.inject.Inject

class RescheduleAlarmWorkerFactory @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val scheduleAlarmManager: ScheduleAlarmManager,
    private val workRequestManager: WorkRequestManager,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return RescheduleAlarmWorker(
            alarmRepository,
            scheduleAlarmManager,
            workRequestManager,
            appContext,
            params
        )
    }
}
