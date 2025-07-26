package ru.ichaporgin.ralearningapp.data

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import ru.ichaporgin.ralearningapp.model.Ingredient
import java.lang.reflect.Method

object Converter {
    @TypeConverter
    fun fromIngredientList(ingredient: List<Ingredient>): String {
        return Json.encodeToString(ingredient)
    }

    @TypeConverter
    fun toIngredientList(ingredient: String): List<Ingredient> {
        return Json.decodeFromString(ingredient)
    }

    @TypeConverter
    fun fromMethodList(method: List<String>): String {
        return Json.encodeToString(method)
    }

    @TypeConverter
    fun toMethodList(method: String): List<String> {
        return Json.decodeFromString(method)
    }
}