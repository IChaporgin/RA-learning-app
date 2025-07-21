package ru.ichaporgin.ralearningapp.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Recipe(
    @PrimaryKey var id: Int,
    var title: String,
    var imageUrl: String,
) {
    @Ignore
    var ingredients: List<Ingredient> = emptyList()

    @Ignore
    var method: List<String> = emptyList()
}