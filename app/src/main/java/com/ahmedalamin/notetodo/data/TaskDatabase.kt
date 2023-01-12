package com.ahmedalamin.notetodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ahmedalamin.notetodo.di.ApplicationScope
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            // db operations
           val dao =  database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task(name = "Hi, you can put your tasks like this! "))
                dao.insert(Task(name = "I wish you a good day \uD83E\uDD70 "))
                dao.insert(Task(name = "Finished tasks will be like that \uD83D\uDCAA ", completed = true))
                dao.insert(Task(name = "Swipe left or right to delete \uD83D\uDC48 \uD83D\uDC49 "))
                dao.insert(Task(name = "You can click on any task to edit ✍\uD83C\uDFFB "))
                dao.insert(Task(name = "Important tasks will have icon and will be at top ", important = true))

            }



        }
    }
}