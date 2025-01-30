package com.example.bluetooth2

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(tableName = "sensors_data")
data class SensorsData(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val time:Int,
    val x:Int,
    val y:Int,
    val z:Int,
    val bpm:Int,
    val spo2:Int,
    val breath:Int
)

@Dao
interface SensorsDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insert(data:SensorsData)

    @Query("SELECT * FROM sensors_data")
    suspend fun getAll():List<SensorsData>

    @Query("DELETE FROM sensors_data")
    suspend fun deleteAll()
}

@Database(
    entities = [SensorsData::class],
    version = 5
)
abstract class SensorsDatabase:RoomDatabase(){
    abstract fun sensorsDao():SensorsDao
}

object DatabaseProvider{
    private var INSTANCE:SensorsDatabase? = null

    fun getDatabase(context:Context):SensorsDatabase{
        if(INSTANCE == null){
            synchronized(SensorsDatabase::class){
                INSTANCE = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = SensorsDatabase::class.java,
                    name = "sensors_data"
                ).addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4,MIGRATION_4_5).build()
            }
        }
        return INSTANCE!!
    }
    private val MIGRATION_1_2 = object:Migration(1,2){
        override fun migrate(db:SupportSQLiteDatabase){
            db.execSQL(sql = "ALTER TABLE sensors_data ADD COLUMN z INTEGER DEFAULT 0 NOT NULL")
        }
    }
    private val MIGRATION_2_3 = object:Migration(2,3){
        override fun migrate(db:SupportSQLiteDatabase){
            db.execSQL(sql = "ALTER TABLE sensors_data ADD COLUMN bpm INTEGER DEFAULT 0 NOT NULL")
            db.execSQL(sql = "ALTER TABLE sensors_data ADD COLUMN spo2 INTEGER DEFAULT 0 NOT NULL")
        }
    }
    private val MIGRATION_3_4 = object:Migration(3,4){
        override fun migrate(db:SupportSQLiteDatabase){
            db.execSQL(sql = "ALTER TABLE sensors_data ADD COLUMN bpm INTEGER DEFAULT 0 NOT NULL")
            db.execSQL(sql = "ALTER TABLE sensors_data ADD COLUMN spo2 INTEGER DEFAULT 0 NOT NULL")
        }
    }
    private val MIGRATION_4_5 = object:Migration(4,5){
        override fun migrate(db:SupportSQLiteDatabase){
            db.execSQL(sql = "ALTER TABLE sensors_data ADD COLUMN breath INTEGER DEFAULT 0 NOT NULL")
        }
    }
}