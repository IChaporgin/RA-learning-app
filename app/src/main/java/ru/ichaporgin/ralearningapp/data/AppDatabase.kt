package ru.ichaporgin.ralearningapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ichaporgin.ralearningapp.model.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}