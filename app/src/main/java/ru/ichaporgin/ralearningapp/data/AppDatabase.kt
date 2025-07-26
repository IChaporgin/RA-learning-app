package ru.ichaporgin.ralearningapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.ichaporgin.ralearningapp.model.Category
import ru.ichaporgin.ralearningapp.model.Recipe
@Database(entities = [Category::class, Recipe::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun recipeDao(): RecipeDao

}