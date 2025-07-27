package ru.ichaporgin.ralearningapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ichaporgin.ralearningapp.model.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * FROM Recipe")
    suspend fun getAllRecipes(): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE isFavorite = 1")
    suspend fun getFavoriteRecipes(): List<Recipe>

    @Query("UPDATE Recipe SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(isFavorite: Boolean, id: Int)

    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    suspend fun getRecipe(recipeId: Int): Recipe?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipes: Recipe)
}