package com.example.snakegameapplication.localdatabase

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase


@Entity
data class SnakeData(

    @PrimaryKey (autoGenerate = true)
    val id : Int? = 0,
    val highscore : Int? = 0
                    )

@Dao
interface SnakeDao
{
   @Query("SELECT * FROM SnakeData")
   suspend fun getHighscore() : SnakeData
}

@Database
(
    entities = [SnakeData::class],
    version = 1
)
abstract class SnakeDatabase : RoomDatabase()
{
    abstract val dao : SnakeDao
}
