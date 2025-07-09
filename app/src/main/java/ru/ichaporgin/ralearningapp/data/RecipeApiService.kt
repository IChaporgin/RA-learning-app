import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ichaporgin.ralearningapp.model.Category
import ru.ichaporgin.ralearningapp.model.Recipe


interface RecipeApiService {
    @GET("recipes")
    fun getRecipes(@Query("ids") ids: String): Call<List<Recipe>>

    @GET("recipe/{id}")
    fun getRecipe(@Path("id") id: Int): Call<Recipe>

    @GET("category/{id}")
    fun getCategory(@Path("id") id: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipesByCategory(@Path("id") id: Int): Call<List<Recipe>>
    @GET("category")
    fun getCategories(): Call<List<Category>>
}