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
                dao.insert(Task(name = "wash cva "))
                dao.insert(Task(name = "washasd f"))
                dao.insert(Task(name = "wash tasdf shes", important = true))
                dao.insert(Task(name = "wae dishes"))
                dao.insert(Task(name = "cvishes", completed = true))
                dao.insert(Task(name = "wache dishes"))
                dao.insert(Task(name = "wash the dishes"))
                dao.insert(Task(name = "waccccccccsh the dishes", completed = true))
                dao.insert(Task(name = "wacvasdfhes"))
            }



        }
    }
}