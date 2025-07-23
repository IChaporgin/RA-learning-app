package ru.ichaporgin.ralearningapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ichaporgin.ralearningapp.model.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * FROM Recipe WHERE categoryId = :categoryId")
    suspend fun getAllRecipes(categoryId: Int): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)
}