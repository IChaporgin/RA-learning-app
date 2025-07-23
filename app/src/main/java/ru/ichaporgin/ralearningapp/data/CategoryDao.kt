package ru.ichaporgin.ralearningapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ichaporgin.ralearningapp.model.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    suspend fun getAllCategory(): List<Category>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: List<Category>)
}