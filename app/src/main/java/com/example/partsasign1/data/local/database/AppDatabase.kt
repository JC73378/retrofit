package com.example.partsasign1.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.partsasign1.data.local.user.UserDao
import com.example.partsasign1.data.local.user.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "parts_app.db"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // AQUÍ VA EL SEED ↓
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getInstance(context).userDao()

                                // Solo insertar si la tabla está vacía
                                if (dao.getByEmail("admin@duoc.cl") == null) {
                                    val seed = listOf(
                                        UserEntity(email = "admin@duoc.cl", password = "Admin123!"),
                                        UserEntity(email = "vcruz@duoc.cl", password = "123456"),
                                        UserEntity(email = "jcarmona@arrimaq.com", password = "Nicoleg12$@"),
                                        UserEntity(email = "maxioporto@arrimaq.com", password = "TiaWaiffu2")
                                    )
                                    seed.forEach { dao.insert(it) }
                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}