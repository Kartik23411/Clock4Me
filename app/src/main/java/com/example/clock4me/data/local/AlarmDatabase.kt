package com.example.clock4me.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.clock4me.data.model.Alarm

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {

    abstract fun getAlarmDao(): AlarmDao
}
