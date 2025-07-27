package ru.ichaporgin.ralearningapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Recipe(
    @PrimaryKey var id: Int = 0,
    var title: String = "",
    var imageUrl: String = "",
    @Transient
    var categoryId: Int? = null,
    var isFavorite: Boolean = false,
    var ingredients: List<Ingredient> = emptyList(),
    var method: List<String> = emptyList(),
)