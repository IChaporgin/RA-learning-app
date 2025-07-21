package ru.ichaporgin.ralearningapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ichaporgin.ralearningapp.model.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe")
    suspend fun getAllRecipes(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)
}