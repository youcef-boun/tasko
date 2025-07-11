package com.youcef_bounaas.tasko.Main.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Task::class],
    version = 2,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TasksDao

    companion object {


        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database" // Database file name
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
               return instance
            }
        }
    }
}


// Migration from version 1 to version 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add the new column 'imagePath' as nullable in the 'tasks' table
        database.execSQL("ALTER TABLE tasks ADD COLUMN imagePath TEXT NOT NULL DEFAULT 'undefined'")
    }
}


//"ALTER TABLE tasks ADD COLUMN imagePath TEXT NOT NULL DEFAULT 'undefined'"
