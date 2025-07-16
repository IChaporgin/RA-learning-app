import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ichaporgin.ralearningapp.model.Category
import ru.ichaporgin.ralearningapp.model.Recipe


interface RecipeApiService {
    @GET("recipes")
    suspend fun getRecipes(@Query("ids") ids: String): List<Recipe>

    @GET("recipe/{id}")
    suspend fun getRecipe(@Path("id") id: Int): Recipe

    @GET("category/{id}")
    suspend fun getCategory(@Path("id") id: Int): Category

    @GET("category/{id}/recipes")
    suspend fun getRecipesByCategory(@Path("id") id: Int): List<Recipe>

    @GET("category")
    suspend fun getCategories(): List<Category>
}