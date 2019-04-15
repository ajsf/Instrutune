package tech.ajsf.instrutune.common.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import io.reactivex.schedulers.Schedulers
import tech.ajsf.instrutune.common.data.InstrumentFactory

@Database(
    entities = [InstrumentEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListTypeConverter::class)
abstract class TunerDatabase : RoomDatabase() {

    abstract fun instrumentDao(): InstrumentDao

    companion object {
        @Volatile
        private var INSTANCE: TunerDatabase? = null

        fun getInstance(context: Context): TunerDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) = Room
            .databaseBuilder(context.applicationContext, TunerDatabase::class.java, "Tuner.db")
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    createInitialInstruments(context)
                }
            })
            .build()

        private fun createInitialInstruments(context: Context) {
            val entities = InstrumentFactory.getDefaultEntities()
            getInstance(context)
                .instrumentDao()
                .insertAll(entities)
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
    }
}